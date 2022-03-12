package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.EItemResult;
import com.ledar.mono.repository.EItemResultRepository;
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
 * Integration tests for the {@link EItemResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EItemResultResourceIT {

    private static final String DEFAULT_E_ITEM_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_E_ITEM_RESULT = "BBBBBBBBBB";

    private static final Long DEFAULT_E_NUMBER = 1L;
    private static final Long UPDATED_E_NUMBER = 2L;

    private static final Long DEFAULT_E_ITEM_NUMBER = 1L;
    private static final Long UPDATED_E_ITEM_NUMBER = 2L;

    private static final Long DEFAULT_E_SUBITEM = 1L;
    private static final Long UPDATED_E_SUBITEM = 2L;

    private static final String ENTITY_API_URL = "/api/e-item-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EItemResultRepository eItemResultRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEItemResultMockMvc;

    private EItemResult eItemResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EItemResult createEntity(EntityManager em) {
        EItemResult eItemResult = new EItemResult()
            .eItemResult(DEFAULT_E_ITEM_RESULT)
            .eNumber(DEFAULT_E_NUMBER)
            .eItemNumber(DEFAULT_E_ITEM_NUMBER)
            .eSubitem(DEFAULT_E_SUBITEM);
        return eItemResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EItemResult createUpdatedEntity(EntityManager em) {
        EItemResult eItemResult = new EItemResult()
            .eItemResult(UPDATED_E_ITEM_RESULT)
            .eNumber(UPDATED_E_NUMBER)
            .eItemNumber(UPDATED_E_ITEM_NUMBER)
            .eSubitem(UPDATED_E_SUBITEM);
        return eItemResult;
    }

    @BeforeEach
    public void initTest() {
        eItemResult = createEntity(em);
    }

    @Test
    @Transactional
    void createEItemResult() throws Exception {
        int databaseSizeBeforeCreate = eItemResultRepository.findAll().size();
        // Create the EItemResult
        restEItemResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItemResult)))
            .andExpect(status().isCreated());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeCreate + 1);
        EItemResult testEItemResult = eItemResultList.get(eItemResultList.size() - 1);
        assertThat(testEItemResult.geteItemResult()).isEqualTo(DEFAULT_E_ITEM_RESULT);
        assertThat(testEItemResult.geteNumber()).isEqualTo(DEFAULT_E_NUMBER);
        assertThat(testEItemResult.geteItemNumber()).isEqualTo(DEFAULT_E_ITEM_NUMBER);
        assertThat(testEItemResult.geteSubitem()).isEqualTo(DEFAULT_E_SUBITEM);
    }

    @Test
    @Transactional
    void createEItemResultWithExistingId() throws Exception {
        // Create the EItemResult with an existing ID
        eItemResult.setId(1L);

        int databaseSizeBeforeCreate = eItemResultRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEItemResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItemResult)))
            .andExpect(status().isBadRequest());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEItemResults() throws Exception {
        // Initialize the database
        eItemResultRepository.saveAndFlush(eItemResult);

        // Get all the eItemResultList
        restEItemResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eItemResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].eItemResult").value(hasItem(DEFAULT_E_ITEM_RESULT)))
            .andExpect(jsonPath("$.[*].eNumber").value(hasItem(DEFAULT_E_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].eItemNumber").value(hasItem(DEFAULT_E_ITEM_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].eSubitem").value(hasItem(DEFAULT_E_SUBITEM.intValue())));
    }

    @Test
    @Transactional
    void getEItemResult() throws Exception {
        // Initialize the database
        eItemResultRepository.saveAndFlush(eItemResult);

        // Get the eItemResult
        restEItemResultMockMvc
            .perform(get(ENTITY_API_URL_ID, eItemResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eItemResult.getId().intValue()))
            .andExpect(jsonPath("$.eItemResult").value(DEFAULT_E_ITEM_RESULT))
            .andExpect(jsonPath("$.eNumber").value(DEFAULT_E_NUMBER.intValue()))
            .andExpect(jsonPath("$.eItemNumber").value(DEFAULT_E_ITEM_NUMBER.intValue()))
            .andExpect(jsonPath("$.eSubitem").value(DEFAULT_E_SUBITEM.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEItemResult() throws Exception {
        // Get the eItemResult
        restEItemResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEItemResult() throws Exception {
        // Initialize the database
        eItemResultRepository.saveAndFlush(eItemResult);

        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();

        // Update the eItemResult
        EItemResult updatedEItemResult = eItemResultRepository.findById(eItemResult.getId()).get();
        // Disconnect from session so that the updates on updatedEItemResult are not directly saved in db
        em.detach(updatedEItemResult);
        updatedEItemResult
            .eItemResult(UPDATED_E_ITEM_RESULT)
            .eNumber(UPDATED_E_NUMBER)
            .eItemNumber(UPDATED_E_ITEM_NUMBER)
            .eSubitem(UPDATED_E_SUBITEM);

        restEItemResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEItemResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEItemResult))
            )
            .andExpect(status().isOk());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
        EItemResult testEItemResult = eItemResultList.get(eItemResultList.size() - 1);
        assertThat(testEItemResult.geteItemResult()).isEqualTo(UPDATED_E_ITEM_RESULT);
        assertThat(testEItemResult.geteNumber()).isEqualTo(UPDATED_E_NUMBER);
        assertThat(testEItemResult.geteItemNumber()).isEqualTo(UPDATED_E_ITEM_NUMBER);
        assertThat(testEItemResult.geteSubitem()).isEqualTo(UPDATED_E_SUBITEM);
    }

    @Test
    @Transactional
    void putNonExistingEItemResult() throws Exception {
        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();
        eItemResult.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEItemResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eItemResult.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eItemResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEItemResult() throws Exception {
        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();
        eItemResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItemResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eItemResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEItemResult() throws Exception {
        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();
        eItemResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItemResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItemResult)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEItemResultWithPatch() throws Exception {
        // Initialize the database
        eItemResultRepository.saveAndFlush(eItemResult);

        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();

        // Update the eItemResult using partial update
        EItemResult partialUpdatedEItemResult = new EItemResult();
        partialUpdatedEItemResult.setId(eItemResult.getId());

        partialUpdatedEItemResult.eNumber(UPDATED_E_NUMBER);

        restEItemResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEItemResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEItemResult))
            )
            .andExpect(status().isOk());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
        EItemResult testEItemResult = eItemResultList.get(eItemResultList.size() - 1);
        assertThat(testEItemResult.geteItemResult()).isEqualTo(DEFAULT_E_ITEM_RESULT);
        assertThat(testEItemResult.geteNumber()).isEqualTo(UPDATED_E_NUMBER);
        assertThat(testEItemResult.geteItemNumber()).isEqualTo(DEFAULT_E_ITEM_NUMBER);
        assertThat(testEItemResult.geteSubitem()).isEqualTo(DEFAULT_E_SUBITEM);
    }

    @Test
    @Transactional
    void fullUpdateEItemResultWithPatch() throws Exception {
        // Initialize the database
        eItemResultRepository.saveAndFlush(eItemResult);

        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();

        // Update the eItemResult using partial update
        EItemResult partialUpdatedEItemResult = new EItemResult();
        partialUpdatedEItemResult.setId(eItemResult.getId());

        partialUpdatedEItemResult
            .eItemResult(UPDATED_E_ITEM_RESULT)
            .eNumber(UPDATED_E_NUMBER)
            .eItemNumber(UPDATED_E_ITEM_NUMBER)
            .eSubitem(UPDATED_E_SUBITEM);

        restEItemResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEItemResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEItemResult))
            )
            .andExpect(status().isOk());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
        EItemResult testEItemResult = eItemResultList.get(eItemResultList.size() - 1);
        assertThat(testEItemResult.geteItemResult()).isEqualTo(UPDATED_E_ITEM_RESULT);
        assertThat(testEItemResult.geteNumber()).isEqualTo(UPDATED_E_NUMBER);
        assertThat(testEItemResult.geteItemNumber()).isEqualTo(UPDATED_E_ITEM_NUMBER);
        assertThat(testEItemResult.geteSubitem()).isEqualTo(UPDATED_E_SUBITEM);
    }

    @Test
    @Transactional
    void patchNonExistingEItemResult() throws Exception {
        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();
        eItemResult.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEItemResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eItemResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eItemResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEItemResult() throws Exception {
        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();
        eItemResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItemResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eItemResult))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEItemResult() throws Exception {
        int databaseSizeBeforeUpdate = eItemResultRepository.findAll().size();
        eItemResult.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItemResultMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eItemResult))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EItemResult in the database
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEItemResult() throws Exception {
        // Initialize the database
        eItemResultRepository.saveAndFlush(eItemResult);

        int databaseSizeBeforeDelete = eItemResultRepository.findAll().size();

        // Delete the eItemResult
        restEItemResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, eItemResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EItemResult> eItemResultList = eItemResultRepository.findAll();
        assertThat(eItemResultList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
