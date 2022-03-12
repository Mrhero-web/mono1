package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.EItem2;
import com.ledar.mono.repository.EItem2Repository;
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
 * Integration tests for the {@link EItem2Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EItem2ResourceIT {

    private static final Long DEFAULT_E_ITEM_RESULT = 1L;
    private static final Long UPDATED_E_ITEM_RESULT = 2L;

    private static final String DEFAULT_E_Z_1 = "AAAAAAAAAA";
    private static final String UPDATED_E_Z_1 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/e-item-2-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EItem2Repository eItem2Repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEItem2MockMvc;

    private EItem2 eItem2;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EItem2 createEntity(EntityManager em) {
        EItem2 eItem2 = new EItem2().eItemResult(DEFAULT_E_ITEM_RESULT).eZ1(DEFAULT_E_Z_1);
        return eItem2;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EItem2 createUpdatedEntity(EntityManager em) {
        EItem2 eItem2 = new EItem2().eItemResult(UPDATED_E_ITEM_RESULT).eZ1(UPDATED_E_Z_1);
        return eItem2;
    }

    @BeforeEach
    public void initTest() {
        eItem2 = createEntity(em);
    }

    @Test
    @Transactional
    void createEItem2() throws Exception {
        int databaseSizeBeforeCreate = eItem2Repository.findAll().size();
        // Create the EItem2
        restEItem2MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItem2)))
            .andExpect(status().isCreated());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeCreate + 1);
        EItem2 testEItem2 = eItem2List.get(eItem2List.size() - 1);
        assertThat(testEItem2.geteItemResult()).isEqualTo(DEFAULT_E_ITEM_RESULT);
        assertThat(testEItem2.geteZ1()).isEqualTo(DEFAULT_E_Z_1);
    }

    @Test
    @Transactional
    void createEItem2WithExistingId() throws Exception {
        // Create the EItem2 with an existing ID
        eItem2.setId(1L);

        int databaseSizeBeforeCreate = eItem2Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEItem2MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItem2)))
            .andExpect(status().isBadRequest());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEItem2s() throws Exception {
        // Initialize the database
        eItem2Repository.saveAndFlush(eItem2);

        // Get all the eItem2List
        restEItem2MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eItem2.getId().intValue())))
            .andExpect(jsonPath("$.[*].eItemResult").value(hasItem(DEFAULT_E_ITEM_RESULT.intValue())))
            .andExpect(jsonPath("$.[*].eZ1").value(hasItem(DEFAULT_E_Z_1)));
    }

    @Test
    @Transactional
    void getEItem2() throws Exception {
        // Initialize the database
        eItem2Repository.saveAndFlush(eItem2);

        // Get the eItem2
        restEItem2MockMvc
            .perform(get(ENTITY_API_URL_ID, eItem2.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eItem2.getId().intValue()))
            .andExpect(jsonPath("$.eItemResult").value(DEFAULT_E_ITEM_RESULT.intValue()))
            .andExpect(jsonPath("$.eZ1").value(DEFAULT_E_Z_1));
    }

    @Test
    @Transactional
    void getNonExistingEItem2() throws Exception {
        // Get the eItem2
        restEItem2MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEItem2() throws Exception {
        // Initialize the database
        eItem2Repository.saveAndFlush(eItem2);

        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();

        // Update the eItem2
        EItem2 updatedEItem2 = eItem2Repository.findById(eItem2.getId()).get();
        // Disconnect from session so that the updates on updatedEItem2 are not directly saved in db
        em.detach(updatedEItem2);
        updatedEItem2.eItemResult(UPDATED_E_ITEM_RESULT).eZ1(UPDATED_E_Z_1);

        restEItem2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEItem2.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEItem2))
            )
            .andExpect(status().isOk());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
        EItem2 testEItem2 = eItem2List.get(eItem2List.size() - 1);
        assertThat(testEItem2.geteItemResult()).isEqualTo(UPDATED_E_ITEM_RESULT);
        assertThat(testEItem2.geteZ1()).isEqualTo(UPDATED_E_Z_1);
    }

    @Test
    @Transactional
    void putNonExistingEItem2() throws Exception {
        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();
        eItem2.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEItem2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, eItem2.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eItem2))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEItem2() throws Exception {
        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();
        eItem2.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem2MockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eItem2))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEItem2() throws Exception {
        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();
        eItem2.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem2MockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItem2)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEItem2WithPatch() throws Exception {
        // Initialize the database
        eItem2Repository.saveAndFlush(eItem2);

        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();

        // Update the eItem2 using partial update
        EItem2 partialUpdatedEItem2 = new EItem2();
        partialUpdatedEItem2.setId(eItem2.getId());

        partialUpdatedEItem2.eZ1(UPDATED_E_Z_1);

        restEItem2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEItem2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEItem2))
            )
            .andExpect(status().isOk());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
        EItem2 testEItem2 = eItem2List.get(eItem2List.size() - 1);
        assertThat(testEItem2.geteItemResult()).isEqualTo(DEFAULT_E_ITEM_RESULT);
        assertThat(testEItem2.geteZ1()).isEqualTo(UPDATED_E_Z_1);
    }

    @Test
    @Transactional
    void fullUpdateEItem2WithPatch() throws Exception {
        // Initialize the database
        eItem2Repository.saveAndFlush(eItem2);

        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();

        // Update the eItem2 using partial update
        EItem2 partialUpdatedEItem2 = new EItem2();
        partialUpdatedEItem2.setId(eItem2.getId());

        partialUpdatedEItem2.eItemResult(UPDATED_E_ITEM_RESULT).eZ1(UPDATED_E_Z_1);

        restEItem2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEItem2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEItem2))
            )
            .andExpect(status().isOk());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
        EItem2 testEItem2 = eItem2List.get(eItem2List.size() - 1);
        assertThat(testEItem2.geteItemResult()).isEqualTo(UPDATED_E_ITEM_RESULT);
        assertThat(testEItem2.geteZ1()).isEqualTo(UPDATED_E_Z_1);
    }

    @Test
    @Transactional
    void patchNonExistingEItem2() throws Exception {
        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();
        eItem2.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEItem2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eItem2.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eItem2))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEItem2() throws Exception {
        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();
        eItem2.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem2MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eItem2))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEItem2() throws Exception {
        int databaseSizeBeforeUpdate = eItem2Repository.findAll().size();
        eItem2.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem2MockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eItem2)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EItem2 in the database
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEItem2() throws Exception {
        // Initialize the database
        eItem2Repository.saveAndFlush(eItem2);

        int databaseSizeBeforeDelete = eItem2Repository.findAll().size();

        // Delete the eItem2
        restEItem2MockMvc
            .perform(delete(ENTITY_API_URL_ID, eItem2.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EItem2> eItem2List = eItem2Repository.findAll();
        assertThat(eItem2List).hasSize(databaseSizeBeforeDelete - 1);
    }
}
