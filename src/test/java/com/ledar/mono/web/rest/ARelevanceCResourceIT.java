package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ARelevanceC;
import com.ledar.mono.repository.ARelevanceCRepository;
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
 * Integration tests for the {@link ARelevanceCResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ARelevanceCResourceIT {

    private static final Long DEFAULT_A_ID = 1L;
    private static final Long UPDATED_A_ID = 2L;

    private static final Long DEFAULT_C_ID = 1L;
    private static final Long UPDATED_C_ID = 2L;

    private static final String ENTITY_API_URL = "/api/a-relevance-cs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ARelevanceCRepository aRelevanceCRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restARelevanceCMockMvc;

    private ARelevanceC aRelevanceC;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ARelevanceC createEntity(EntityManager em) {
        ARelevanceC aRelevanceC = new ARelevanceC().aId(DEFAULT_A_ID).cId(DEFAULT_C_ID);
        return aRelevanceC;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ARelevanceC createUpdatedEntity(EntityManager em) {
        ARelevanceC aRelevanceC = new ARelevanceC().aId(UPDATED_A_ID).cId(UPDATED_C_ID);
        return aRelevanceC;
    }

    @BeforeEach
    public void initTest() {
        aRelevanceC = createEntity(em);
    }

    @Test
    @Transactional
    void createARelevanceC() throws Exception {
        int databaseSizeBeforeCreate = aRelevanceCRepository.findAll().size();
        // Create the ARelevanceC
        restARelevanceCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aRelevanceC)))
            .andExpect(status().isCreated());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeCreate + 1);
        ARelevanceC testARelevanceC = aRelevanceCList.get(aRelevanceCList.size() - 1);
        assertThat(testARelevanceC.getaId()).isEqualTo(DEFAULT_A_ID);
        assertThat(testARelevanceC.getcId()).isEqualTo(DEFAULT_C_ID);
    }

    @Test
    @Transactional
    void createARelevanceCWithExistingId() throws Exception {
        // Create the ARelevanceC with an existing ID
        aRelevanceC.setId(1L);

        int databaseSizeBeforeCreate = aRelevanceCRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restARelevanceCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aRelevanceC)))
            .andExpect(status().isBadRequest());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllARelevanceCS() throws Exception {
        // Initialize the database
        aRelevanceCRepository.saveAndFlush(aRelevanceC);

        // Get all the aRelevanceCList
        restARelevanceCMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aRelevanceC.getId().intValue())))
            .andExpect(jsonPath("$.[*].aId").value(hasItem(DEFAULT_A_ID.intValue())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.intValue())));
    }

    @Test
    @Transactional
    void getARelevanceC() throws Exception {
        // Initialize the database
        aRelevanceCRepository.saveAndFlush(aRelevanceC);

        // Get the aRelevanceC
        restARelevanceCMockMvc
            .perform(get(ENTITY_API_URL_ID, aRelevanceC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aRelevanceC.getId().intValue()))
            .andExpect(jsonPath("$.aId").value(DEFAULT_A_ID.intValue()))
            .andExpect(jsonPath("$.cId").value(DEFAULT_C_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingARelevanceC() throws Exception {
        // Get the aRelevanceC
        restARelevanceCMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewARelevanceC() throws Exception {
        // Initialize the database
        aRelevanceCRepository.saveAndFlush(aRelevanceC);

        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();

        // Update the aRelevanceC
        ARelevanceC updatedARelevanceC = aRelevanceCRepository.findById(aRelevanceC.getId()).get();
        // Disconnect from session so that the updates on updatedARelevanceC are not directly saved in db
        em.detach(updatedARelevanceC);
        updatedARelevanceC.aId(UPDATED_A_ID).cId(UPDATED_C_ID);

        restARelevanceCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedARelevanceC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedARelevanceC))
            )
            .andExpect(status().isOk());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
        ARelevanceC testARelevanceC = aRelevanceCList.get(aRelevanceCList.size() - 1);
        assertThat(testARelevanceC.getaId()).isEqualTo(UPDATED_A_ID);
        assertThat(testARelevanceC.getcId()).isEqualTo(UPDATED_C_ID);
    }

    @Test
    @Transactional
    void putNonExistingARelevanceC() throws Exception {
        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();
        aRelevanceC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restARelevanceCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aRelevanceC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aRelevanceC))
            )
            .andExpect(status().isBadRequest());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchARelevanceC() throws Exception {
        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();
        aRelevanceC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restARelevanceCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(aRelevanceC))
            )
            .andExpect(status().isBadRequest());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamARelevanceC() throws Exception {
        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();
        aRelevanceC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restARelevanceCMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(aRelevanceC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateARelevanceCWithPatch() throws Exception {
        // Initialize the database
        aRelevanceCRepository.saveAndFlush(aRelevanceC);

        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();

        // Update the aRelevanceC using partial update
        ARelevanceC partialUpdatedARelevanceC = new ARelevanceC();
        partialUpdatedARelevanceC.setId(aRelevanceC.getId());

        restARelevanceCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedARelevanceC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedARelevanceC))
            )
            .andExpect(status().isOk());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
        ARelevanceC testARelevanceC = aRelevanceCList.get(aRelevanceCList.size() - 1);
        assertThat(testARelevanceC.getaId()).isEqualTo(DEFAULT_A_ID);
        assertThat(testARelevanceC.getcId()).isEqualTo(DEFAULT_C_ID);
    }

    @Test
    @Transactional
    void fullUpdateARelevanceCWithPatch() throws Exception {
        // Initialize the database
        aRelevanceCRepository.saveAndFlush(aRelevanceC);

        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();

        // Update the aRelevanceC using partial update
        ARelevanceC partialUpdatedARelevanceC = new ARelevanceC();
        partialUpdatedARelevanceC.setId(aRelevanceC.getId());

        partialUpdatedARelevanceC.aId(UPDATED_A_ID).cId(UPDATED_C_ID);

        restARelevanceCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedARelevanceC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedARelevanceC))
            )
            .andExpect(status().isOk());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
        ARelevanceC testARelevanceC = aRelevanceCList.get(aRelevanceCList.size() - 1);
        assertThat(testARelevanceC.getaId()).isEqualTo(UPDATED_A_ID);
        assertThat(testARelevanceC.getcId()).isEqualTo(UPDATED_C_ID);
    }

    @Test
    @Transactional
    void patchNonExistingARelevanceC() throws Exception {
        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();
        aRelevanceC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restARelevanceCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aRelevanceC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aRelevanceC))
            )
            .andExpect(status().isBadRequest());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchARelevanceC() throws Exception {
        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();
        aRelevanceC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restARelevanceCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(aRelevanceC))
            )
            .andExpect(status().isBadRequest());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamARelevanceC() throws Exception {
        int databaseSizeBeforeUpdate = aRelevanceCRepository.findAll().size();
        aRelevanceC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restARelevanceCMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(aRelevanceC))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ARelevanceC in the database
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteARelevanceC() throws Exception {
        // Initialize the database
        aRelevanceCRepository.saveAndFlush(aRelevanceC);

        int databaseSizeBeforeDelete = aRelevanceCRepository.findAll().size();

        // Delete the aRelevanceC
        restARelevanceCMockMvc
            .perform(delete(ENTITY_API_URL_ID, aRelevanceC.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ARelevanceC> aRelevanceCList = aRelevanceCRepository.findAll();
        assertThat(aRelevanceCList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
