package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.TreatmentProgram;
import com.ledar.mono.repository.TreatmentProgramRepository;
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
 * Integration tests for the {@link TreatmentProgramResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TreatmentProgramResourceIT {

    private static final String DEFAULT_CURE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMS = "AAAAAAAAAA";
    private static final String UPDATED_NORMS = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_CHARGE = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final String ENTITY_API_URL = "/api/treatment-programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TreatmentProgramRepository treatmentProgramRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTreatmentProgramMockMvc;

    private TreatmentProgram treatmentProgram;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TreatmentProgram createEntity(EntityManager em) {
        TreatmentProgram treatmentProgram = new TreatmentProgram()
            .cureName(DEFAULT_CURE_NAME)
            .norms(DEFAULT_NORMS)
            .unit(DEFAULT_UNIT)
            .charge(DEFAULT_CHARGE)
            .price(DEFAULT_PRICE);
        return treatmentProgram;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TreatmentProgram createUpdatedEntity(EntityManager em) {
        TreatmentProgram treatmentProgram = new TreatmentProgram()
            .cureName(UPDATED_CURE_NAME)
            .norms(UPDATED_NORMS)
            .unit(UPDATED_UNIT)
            .charge(UPDATED_CHARGE)
            .price(UPDATED_PRICE);
        return treatmentProgram;
    }

    @BeforeEach
    public void initTest() {
        treatmentProgram = createEntity(em);
    }

    @Test
    @Transactional
    void createTreatmentProgram() throws Exception {
        int databaseSizeBeforeCreate = treatmentProgramRepository.findAll().size();
        // Create the TreatmentProgram
        restTreatmentProgramMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isCreated());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeCreate + 1);
        TreatmentProgram testTreatmentProgram = treatmentProgramList.get(treatmentProgramList.size() - 1);
        assertThat(testTreatmentProgram.getCureName()).isEqualTo(DEFAULT_CURE_NAME);
        assertThat(testTreatmentProgram.getNorms()).isEqualTo(DEFAULT_NORMS);
        assertThat(testTreatmentProgram.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testTreatmentProgram.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testTreatmentProgram.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createTreatmentProgramWithExistingId() throws Exception {
        // Create the TreatmentProgram with an existing ID
        treatmentProgram.setId(1L);

        int databaseSizeBeforeCreate = treatmentProgramRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTreatmentProgramMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTreatmentPrograms() throws Exception {
        // Initialize the database
        treatmentProgramRepository.saveAndFlush(treatmentProgram);

        // Get all the treatmentProgramList
        restTreatmentProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(treatmentProgram.getId().intValue())))
            .andExpect(jsonPath("$.[*].cureName").value(hasItem(DEFAULT_CURE_NAME)))
            .andExpect(jsonPath("$.[*].norms").value(hasItem(DEFAULT_NORMS)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getTreatmentProgram() throws Exception {
        // Initialize the database
        treatmentProgramRepository.saveAndFlush(treatmentProgram);

        // Get the treatmentProgram
        restTreatmentProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, treatmentProgram.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(treatmentProgram.getId().intValue()))
            .andExpect(jsonPath("$.cureName").value(DEFAULT_CURE_NAME))
            .andExpect(jsonPath("$.norms").value(DEFAULT_NORMS))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingTreatmentProgram() throws Exception {
        // Get the treatmentProgram
        restTreatmentProgramMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTreatmentProgram() throws Exception {
        // Initialize the database
        treatmentProgramRepository.saveAndFlush(treatmentProgram);

        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();

        // Update the treatmentProgram
        TreatmentProgram updatedTreatmentProgram = treatmentProgramRepository.findById(treatmentProgram.getId()).get();
        // Disconnect from session so that the updates on updatedTreatmentProgram are not directly saved in db
        em.detach(updatedTreatmentProgram);
        updatedTreatmentProgram
            .cureName(UPDATED_CURE_NAME)
            .norms(UPDATED_NORMS)
            .unit(UPDATED_UNIT)
            .charge(UPDATED_CHARGE)
            .price(UPDATED_PRICE);

        restTreatmentProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTreatmentProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTreatmentProgram))
            )
            .andExpect(status().isOk());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
        TreatmentProgram testTreatmentProgram = treatmentProgramList.get(treatmentProgramList.size() - 1);
        assertThat(testTreatmentProgram.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testTreatmentProgram.getNorms()).isEqualTo(UPDATED_NORMS);
        assertThat(testTreatmentProgram.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testTreatmentProgram.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testTreatmentProgram.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingTreatmentProgram() throws Exception {
        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();
        treatmentProgram.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatmentProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, treatmentProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTreatmentProgram() throws Exception {
        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();
        treatmentProgram.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatmentProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTreatmentProgram() throws Exception {
        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();
        treatmentProgram.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatmentProgramMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTreatmentProgramWithPatch() throws Exception {
        // Initialize the database
        treatmentProgramRepository.saveAndFlush(treatmentProgram);

        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();

        // Update the treatmentProgram using partial update
        TreatmentProgram partialUpdatedTreatmentProgram = new TreatmentProgram();
        partialUpdatedTreatmentProgram.setId(treatmentProgram.getId());

        partialUpdatedTreatmentProgram.unit(UPDATED_UNIT).charge(UPDATED_CHARGE);

        restTreatmentProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTreatmentProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTreatmentProgram))
            )
            .andExpect(status().isOk());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
        TreatmentProgram testTreatmentProgram = treatmentProgramList.get(treatmentProgramList.size() - 1);
        assertThat(testTreatmentProgram.getCureName()).isEqualTo(DEFAULT_CURE_NAME);
        assertThat(testTreatmentProgram.getNorms()).isEqualTo(DEFAULT_NORMS);
        assertThat(testTreatmentProgram.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testTreatmentProgram.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testTreatmentProgram.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateTreatmentProgramWithPatch() throws Exception {
        // Initialize the database
        treatmentProgramRepository.saveAndFlush(treatmentProgram);

        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();

        // Update the treatmentProgram using partial update
        TreatmentProgram partialUpdatedTreatmentProgram = new TreatmentProgram();
        partialUpdatedTreatmentProgram.setId(treatmentProgram.getId());

        partialUpdatedTreatmentProgram
            .cureName(UPDATED_CURE_NAME)
            .norms(UPDATED_NORMS)
            .unit(UPDATED_UNIT)
            .charge(UPDATED_CHARGE)
            .price(UPDATED_PRICE);

        restTreatmentProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTreatmentProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTreatmentProgram))
            )
            .andExpect(status().isOk());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
        TreatmentProgram testTreatmentProgram = treatmentProgramList.get(treatmentProgramList.size() - 1);
        assertThat(testTreatmentProgram.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testTreatmentProgram.getNorms()).isEqualTo(UPDATED_NORMS);
        assertThat(testTreatmentProgram.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testTreatmentProgram.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testTreatmentProgram.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingTreatmentProgram() throws Exception {
        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();
        treatmentProgram.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTreatmentProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, treatmentProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTreatmentProgram() throws Exception {
        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();
        treatmentProgram.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatmentProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTreatmentProgram() throws Exception {
        int databaseSizeBeforeUpdate = treatmentProgramRepository.findAll().size();
        treatmentProgram.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTreatmentProgramMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(treatmentProgram))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TreatmentProgram in the database
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTreatmentProgram() throws Exception {
        // Initialize the database
        treatmentProgramRepository.saveAndFlush(treatmentProgram);

        int databaseSizeBeforeDelete = treatmentProgramRepository.findAll().size();

        // Delete the treatmentProgram
        restTreatmentProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, treatmentProgram.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TreatmentProgram> treatmentProgramList = treatmentProgramRepository.findAll();
        assertThat(treatmentProgramList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
