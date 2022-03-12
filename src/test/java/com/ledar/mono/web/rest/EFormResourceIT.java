package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.EForm;
import com.ledar.mono.domain.enumeration.Category;
import com.ledar.mono.repository.EFormRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EFormResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EFormResourceIT {

    private static final Long DEFAULT_CURE_ID = 1L;
    private static final Long UPDATED_CURE_ID = 2L;

    private static final String DEFAULT_ID_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUM = "BBBBBBBBBB";

    private static final Category DEFAULT_E_CATEGORY = Category.ENTER;
    private static final Category UPDATED_E_CATEGORY = Category.STAGE;

    private static final Long DEFAULT_STAFF_ID = 1L;
    private static final Long UPDATED_STAFF_ID = 2L;

    private static final String DEFAULT_E_CONCLUSION = "AAAAAAAAAA";
    private static final String UPDATED_E_CONCLUSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_E_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_E_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_E_ILLNESS = "AAAAAAAAAA";
    private static final String UPDATED_E_ILLNESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/e-forms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EFormRepository eFormRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEFormMockMvc;

    private EForm eForm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EForm createEntity(EntityManager em) {
        EForm eForm = new EForm()
            .cureId(DEFAULT_CURE_ID)
            .idNum(DEFAULT_ID_NUM)
            .eCategory(DEFAULT_E_CATEGORY)
            .staffId(DEFAULT_STAFF_ID)
            .eConclusion(DEFAULT_E_CONCLUSION)
            .eTime(DEFAULT_E_TIME)
            .eIllness(DEFAULT_E_ILLNESS);
        return eForm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EForm createUpdatedEntity(EntityManager em) {
        EForm eForm = new EForm()
            .cureId(UPDATED_CURE_ID)
            .idNum(UPDATED_ID_NUM)
            .eCategory(UPDATED_E_CATEGORY)
            .staffId(UPDATED_STAFF_ID)
            .eConclusion(UPDATED_E_CONCLUSION)
            .eTime(UPDATED_E_TIME)
            .eIllness(UPDATED_E_ILLNESS);
        return eForm;
    }

    @BeforeEach
    public void initTest() {
        eForm = createEntity(em);
    }

    @Test
    @Transactional
    void createEForm() throws Exception {
        int databaseSizeBeforeCreate = eFormRepository.findAll().size();
        // Create the EForm
        restEFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eForm)))
            .andExpect(status().isCreated());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeCreate + 1);
        EForm testEForm = eFormList.get(eFormList.size() - 1);
        assertThat(testEForm.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testEForm.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testEForm.geteCategory()).isEqualTo(DEFAULT_E_CATEGORY);
        assertThat(testEForm.getStaffId()).isEqualTo(DEFAULT_STAFF_ID);
        assertThat(testEForm.geteConclusion()).isEqualTo(DEFAULT_E_CONCLUSION);
        assertThat(testEForm.geteTime()).isEqualTo(DEFAULT_E_TIME);
        assertThat(testEForm.geteIllness()).isEqualTo(DEFAULT_E_ILLNESS);
    }

    @Test
    @Transactional
    void createEFormWithExistingId() throws Exception {
        // Create the EForm with an existing ID
        eForm.setId(1L);

        int databaseSizeBeforeCreate = eFormRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEFormMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eForm)))
            .andExpect(status().isBadRequest());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEForms() throws Exception {
        // Initialize the database
        eFormRepository.saveAndFlush(eForm);

        // Get all the eFormList
        restEFormMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eForm.getId().intValue())))
            .andExpect(jsonPath("$.[*].cureId").value(hasItem(DEFAULT_CURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].idNum").value(hasItem(DEFAULT_ID_NUM)))
            .andExpect(jsonPath("$.[*].eCategory").value(hasItem(DEFAULT_E_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].staffId").value(hasItem(DEFAULT_STAFF_ID.intValue())))
            .andExpect(jsonPath("$.[*].eConclusion").value(hasItem(DEFAULT_E_CONCLUSION)))
            .andExpect(jsonPath("$.[*].eTime").value(hasItem(DEFAULT_E_TIME.toString())))
            .andExpect(jsonPath("$.[*].eIllness").value(hasItem(DEFAULT_E_ILLNESS)));
    }

    @Test
    @Transactional
    void getEForm() throws Exception {
        // Initialize the database
        eFormRepository.saveAndFlush(eForm);

        // Get the eForm
        restEFormMockMvc
            .perform(get(ENTITY_API_URL_ID, eForm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eForm.getId().intValue()))
            .andExpect(jsonPath("$.cureId").value(DEFAULT_CURE_ID.intValue()))
            .andExpect(jsonPath("$.idNum").value(DEFAULT_ID_NUM))
            .andExpect(jsonPath("$.eCategory").value(DEFAULT_E_CATEGORY.toString()))
            .andExpect(jsonPath("$.staffId").value(DEFAULT_STAFF_ID.intValue()))
            .andExpect(jsonPath("$.eConclusion").value(DEFAULT_E_CONCLUSION))
            .andExpect(jsonPath("$.eTime").value(DEFAULT_E_TIME.toString()))
            .andExpect(jsonPath("$.eIllness").value(DEFAULT_E_ILLNESS));
    }

    @Test
    @Transactional
    void getNonExistingEForm() throws Exception {
        // Get the eForm
        restEFormMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEForm() throws Exception {
        // Initialize the database
        eFormRepository.saveAndFlush(eForm);

        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();

        // Update the eForm
        EForm updatedEForm = eFormRepository.findById(eForm.getId()).get();
        // Disconnect from session so that the updates on updatedEForm are not directly saved in db
        em.detach(updatedEForm);
        updatedEForm
            .cureId(UPDATED_CURE_ID)
            .idNum(UPDATED_ID_NUM)
            .eCategory(UPDATED_E_CATEGORY)
            .staffId(UPDATED_STAFF_ID)
            .eConclusion(UPDATED_E_CONCLUSION)
            .eTime(UPDATED_E_TIME)
            .eIllness(UPDATED_E_ILLNESS);

        restEFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEForm))
            )
            .andExpect(status().isOk());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
        EForm testEForm = eFormList.get(eFormList.size() - 1);
        assertThat(testEForm.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testEForm.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testEForm.geteCategory()).isEqualTo(UPDATED_E_CATEGORY);
        assertThat(testEForm.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testEForm.geteConclusion()).isEqualTo(UPDATED_E_CONCLUSION);
        assertThat(testEForm.geteTime()).isEqualTo(UPDATED_E_TIME);
        assertThat(testEForm.geteIllness()).isEqualTo(UPDATED_E_ILLNESS);
    }

    @Test
    @Transactional
    void putNonExistingEForm() throws Exception {
        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();
        eForm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eForm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEForm() throws Exception {
        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();
        eForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEFormMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEForm() throws Exception {
        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();
        eForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEFormMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eForm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEFormWithPatch() throws Exception {
        // Initialize the database
        eFormRepository.saveAndFlush(eForm);

        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();

        // Update the eForm using partial update
        EForm partialUpdatedEForm = new EForm();
        partialUpdatedEForm.setId(eForm.getId());

        partialUpdatedEForm
            .eCategory(UPDATED_E_CATEGORY)
            .eConclusion(UPDATED_E_CONCLUSION)
            .eTime(UPDATED_E_TIME)
            .eIllness(UPDATED_E_ILLNESS);

        restEFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEForm))
            )
            .andExpect(status().isOk());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
        EForm testEForm = eFormList.get(eFormList.size() - 1);
        assertThat(testEForm.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testEForm.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testEForm.geteCategory()).isEqualTo(UPDATED_E_CATEGORY);
        assertThat(testEForm.getStaffId()).isEqualTo(DEFAULT_STAFF_ID);
        assertThat(testEForm.geteConclusion()).isEqualTo(UPDATED_E_CONCLUSION);
        assertThat(testEForm.geteTime()).isEqualTo(UPDATED_E_TIME);
        assertThat(testEForm.geteIllness()).isEqualTo(UPDATED_E_ILLNESS);
    }

    @Test
    @Transactional
    void fullUpdateEFormWithPatch() throws Exception {
        // Initialize the database
        eFormRepository.saveAndFlush(eForm);

        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();

        // Update the eForm using partial update
        EForm partialUpdatedEForm = new EForm();
        partialUpdatedEForm.setId(eForm.getId());

        partialUpdatedEForm
            .cureId(UPDATED_CURE_ID)
            .idNum(UPDATED_ID_NUM)
            .eCategory(UPDATED_E_CATEGORY)
            .staffId(UPDATED_STAFF_ID)
            .eConclusion(UPDATED_E_CONCLUSION)
            .eTime(UPDATED_E_TIME)
            .eIllness(UPDATED_E_ILLNESS);

        restEFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEForm))
            )
            .andExpect(status().isOk());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
        EForm testEForm = eFormList.get(eFormList.size() - 1);
        assertThat(testEForm.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testEForm.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testEForm.geteCategory()).isEqualTo(UPDATED_E_CATEGORY);
        assertThat(testEForm.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testEForm.geteConclusion()).isEqualTo(UPDATED_E_CONCLUSION);
        assertThat(testEForm.geteTime()).isEqualTo(UPDATED_E_TIME);
        assertThat(testEForm.geteIllness()).isEqualTo(UPDATED_E_ILLNESS);
    }

    @Test
    @Transactional
    void patchNonExistingEForm() throws Exception {
        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();
        eForm.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eForm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEForm() throws Exception {
        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();
        eForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEFormMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eForm))
            )
            .andExpect(status().isBadRequest());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEForm() throws Exception {
        int databaseSizeBeforeUpdate = eFormRepository.findAll().size();
        eForm.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEFormMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eForm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EForm in the database
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEForm() throws Exception {
        // Initialize the database
        eFormRepository.saveAndFlush(eForm);

        int databaseSizeBeforeDelete = eFormRepository.findAll().size();

        // Delete the eForm
        restEFormMockMvc
            .perform(delete(ENTITY_API_URL_ID, eForm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EForm> eFormList = eFormRepository.findAll();
        assertThat(eFormList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
