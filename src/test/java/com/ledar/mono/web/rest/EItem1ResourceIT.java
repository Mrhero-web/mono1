package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.EItem1;
import com.ledar.mono.repository.EItem1Repository;
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
 * Integration tests for the {@link EItem1Resource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EItem1ResourceIT {

    private static final Long DEFAULT_E_ITEM_RESULT = 1L;
    private static final Long UPDATED_E_ITEM_RESULT = 2L;

    private static final String DEFAULT_E_Z_1 = "AAAAAAAAAA";
    private static final String UPDATED_E_Z_1 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/e-item-1-s";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EItem1Repository eItem1Repository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEItem1MockMvc;

    private EItem1 eItem1;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EItem1 createEntity(EntityManager em) {
        EItem1 eItem1 = new EItem1().eItemResult(DEFAULT_E_ITEM_RESULT).eZ1(DEFAULT_E_Z_1);
        return eItem1;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EItem1 createUpdatedEntity(EntityManager em) {
        EItem1 eItem1 = new EItem1().eItemResult(UPDATED_E_ITEM_RESULT).eZ1(UPDATED_E_Z_1);
        return eItem1;
    }

    @BeforeEach
    public void initTest() {
        eItem1 = createEntity(em);
    }

    @Test
    @Transactional
    void createEItem1() throws Exception {
        int databaseSizeBeforeCreate = eItem1Repository.findAll().size();
        // Create the EItem1
        restEItem1MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItem1)))
            .andExpect(status().isCreated());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeCreate + 1);
        EItem1 testEItem1 = eItem1List.get(eItem1List.size() - 1);
        assertThat(testEItem1.geteItemResult()).isEqualTo(DEFAULT_E_ITEM_RESULT);
        assertThat(testEItem1.geteZ1()).isEqualTo(DEFAULT_E_Z_1);
    }

    @Test
    @Transactional
    void createEItem1WithExistingId() throws Exception {
        // Create the EItem1 with an existing ID
        eItem1.setId(1L);

        int databaseSizeBeforeCreate = eItem1Repository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEItem1MockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItem1)))
            .andExpect(status().isBadRequest());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEItem1s() throws Exception {
        // Initialize the database
        eItem1Repository.saveAndFlush(eItem1);

        // Get all the eItem1List
        restEItem1MockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eItem1.getId().intValue())))
            .andExpect(jsonPath("$.[*].eItemResult").value(hasItem(DEFAULT_E_ITEM_RESULT.intValue())))
            .andExpect(jsonPath("$.[*].eZ1").value(hasItem(DEFAULT_E_Z_1)));
    }

    @Test
    @Transactional
    void getEItem1() throws Exception {
        // Initialize the database
        eItem1Repository.saveAndFlush(eItem1);

        // Get the eItem1
        restEItem1MockMvc
            .perform(get(ENTITY_API_URL_ID, eItem1.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eItem1.getId().intValue()))
            .andExpect(jsonPath("$.eItemResult").value(DEFAULT_E_ITEM_RESULT.intValue()))
            .andExpect(jsonPath("$.eZ1").value(DEFAULT_E_Z_1));
    }

    @Test
    @Transactional
    void getNonExistingEItem1() throws Exception {
        // Get the eItem1
        restEItem1MockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEItem1() throws Exception {
        // Initialize the database
        eItem1Repository.saveAndFlush(eItem1);

        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();

        // Update the eItem1
        EItem1 updatedEItem1 = eItem1Repository.findById(eItem1.getId()).get();
        // Disconnect from session so that the updates on updatedEItem1 are not directly saved in db
        em.detach(updatedEItem1);
        updatedEItem1.eItemResult(UPDATED_E_ITEM_RESULT).eZ1(UPDATED_E_Z_1);

        restEItem1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEItem1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEItem1))
            )
            .andExpect(status().isOk());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
        EItem1 testEItem1 = eItem1List.get(eItem1List.size() - 1);
        assertThat(testEItem1.geteItemResult()).isEqualTo(UPDATED_E_ITEM_RESULT);
        assertThat(testEItem1.geteZ1()).isEqualTo(UPDATED_E_Z_1);
    }

    @Test
    @Transactional
    void putNonExistingEItem1() throws Exception {
        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();
        eItem1.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEItem1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, eItem1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eItem1))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEItem1() throws Exception {
        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();
        eItem1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem1MockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eItem1))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEItem1() throws Exception {
        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();
        eItem1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem1MockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eItem1)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEItem1WithPatch() throws Exception {
        // Initialize the database
        eItem1Repository.saveAndFlush(eItem1);

        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();

        // Update the eItem1 using partial update
        EItem1 partialUpdatedEItem1 = new EItem1();
        partialUpdatedEItem1.setId(eItem1.getId());

        partialUpdatedEItem1.eZ1(UPDATED_E_Z_1);

        restEItem1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEItem1.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEItem1))
            )
            .andExpect(status().isOk());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
        EItem1 testEItem1 = eItem1List.get(eItem1List.size() - 1);
        assertThat(testEItem1.geteItemResult()).isEqualTo(DEFAULT_E_ITEM_RESULT);
        assertThat(testEItem1.geteZ1()).isEqualTo(UPDATED_E_Z_1);
    }

    @Test
    @Transactional
    void fullUpdateEItem1WithPatch() throws Exception {
        // Initialize the database
        eItem1Repository.saveAndFlush(eItem1);

        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();

        // Update the eItem1 using partial update
        EItem1 partialUpdatedEItem1 = new EItem1();
        partialUpdatedEItem1.setId(eItem1.getId());

        partialUpdatedEItem1.eItemResult(UPDATED_E_ITEM_RESULT).eZ1(UPDATED_E_Z_1);

        restEItem1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEItem1.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEItem1))
            )
            .andExpect(status().isOk());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
        EItem1 testEItem1 = eItem1List.get(eItem1List.size() - 1);
        assertThat(testEItem1.geteItemResult()).isEqualTo(UPDATED_E_ITEM_RESULT);
        assertThat(testEItem1.geteZ1()).isEqualTo(UPDATED_E_Z_1);
    }

    @Test
    @Transactional
    void patchNonExistingEItem1() throws Exception {
        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();
        eItem1.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEItem1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eItem1.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eItem1))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEItem1() throws Exception {
        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();
        eItem1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem1MockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eItem1))
            )
            .andExpect(status().isBadRequest());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEItem1() throws Exception {
        int databaseSizeBeforeUpdate = eItem1Repository.findAll().size();
        eItem1.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEItem1MockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eItem1)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EItem1 in the database
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEItem1() throws Exception {
        // Initialize the database
        eItem1Repository.saveAndFlush(eItem1);

        int databaseSizeBeforeDelete = eItem1Repository.findAll().size();

        // Delete the eItem1
        restEItem1MockMvc
            .perform(delete(ENTITY_API_URL_ID, eItem1.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EItem1> eItem1List = eItem1Repository.findAll();
        assertThat(eItem1List).hasSize(databaseSizeBeforeDelete - 1);
    }
}
