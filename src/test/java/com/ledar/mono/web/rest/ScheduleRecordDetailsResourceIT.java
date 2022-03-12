package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ScheduleRecordDetails;
import com.ledar.mono.repository.ScheduleRecordDetailsRepository;
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
 * Integration tests for the {@link ScheduleRecordDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleRecordDetailsResourceIT {

    private static final String DEFAULT_D_NAME = "AAAAAAAAAA";
    private static final String UPDATED_D_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CURE_ID = 1L;
    private static final Long UPDATED_CURE_ID = 2L;

    private static final Long DEFAULT_MEDICAL_NUMBER = 1L;
    private static final Long UPDATED_MEDICAL_NUMBER = 2L;

    private static final Long DEFAULT_D_NUM = 1L;
    private static final Long UPDATED_D_NUM = 2L;

    private static final String DEFAULT_ID_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/schedule-record-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduleRecordDetailsRepository scheduleRecordDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleRecordDetailsMockMvc;

    private ScheduleRecordDetails scheduleRecordDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordDetails createEntity(EntityManager em) {
        ScheduleRecordDetails scheduleRecordDetails = new ScheduleRecordDetails()
            .dName(DEFAULT_D_NAME)
            .cureId(DEFAULT_CURE_ID)
            .medicalNumber(DEFAULT_MEDICAL_NUMBER)
            .dNum(DEFAULT_D_NUM)
            .idNum(DEFAULT_ID_NUM);
        return scheduleRecordDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordDetails createUpdatedEntity(EntityManager em) {
        ScheduleRecordDetails scheduleRecordDetails = new ScheduleRecordDetails()
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);
        return scheduleRecordDetails;
    }

    @BeforeEach
    public void initTest() {
        scheduleRecordDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createScheduleRecordDetails() throws Exception {
        int databaseSizeBeforeCreate = scheduleRecordDetailsRepository.findAll().size();
        // Create the ScheduleRecordDetails
        restScheduleRecordDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isCreated());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleRecordDetails testScheduleRecordDetails = scheduleRecordDetailsList.get(scheduleRecordDetailsList.size() - 1);
        assertThat(testScheduleRecordDetails.getdName()).isEqualTo(DEFAULT_D_NAME);
        assertThat(testScheduleRecordDetails.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordDetails.getMedicalNumber()).isEqualTo(DEFAULT_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetails.getdNum()).isEqualTo(DEFAULT_D_NUM);
        assertThat(testScheduleRecordDetails.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
    }

    @Test
    @Transactional
    void createScheduleRecordDetailsWithExistingId() throws Exception {
        // Create the ScheduleRecordDetails with an existing ID
        scheduleRecordDetails.setId(1L);

        int databaseSizeBeforeCreate = scheduleRecordDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleRecordDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScheduleRecordDetails() throws Exception {
        // Initialize the database
        scheduleRecordDetailsRepository.saveAndFlush(scheduleRecordDetails);

        // Get all the scheduleRecordDetailsList
        restScheduleRecordDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleRecordDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].dName").value(hasItem(DEFAULT_D_NAME)))
            .andExpect(jsonPath("$.[*].cureId").value(hasItem(DEFAULT_CURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].medicalNumber").value(hasItem(DEFAULT_MEDICAL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].dNum").value(hasItem(DEFAULT_D_NUM.intValue())))
            .andExpect(jsonPath("$.[*].idNum").value(hasItem(DEFAULT_ID_NUM)));
    }

    @Test
    @Transactional
    void getScheduleRecordDetails() throws Exception {
        // Initialize the database
        scheduleRecordDetailsRepository.saveAndFlush(scheduleRecordDetails);

        // Get the scheduleRecordDetails
        restScheduleRecordDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleRecordDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleRecordDetails.getId().intValue()))
            .andExpect(jsonPath("$.dName").value(DEFAULT_D_NAME))
            .andExpect(jsonPath("$.cureId").value(DEFAULT_CURE_ID.intValue()))
            .andExpect(jsonPath("$.medicalNumber").value(DEFAULT_MEDICAL_NUMBER.intValue()))
            .andExpect(jsonPath("$.dNum").value(DEFAULT_D_NUM.intValue()))
            .andExpect(jsonPath("$.idNum").value(DEFAULT_ID_NUM));
    }

    @Test
    @Transactional
    void getNonExistingScheduleRecordDetails() throws Exception {
        // Get the scheduleRecordDetails
        restScheduleRecordDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheduleRecordDetails() throws Exception {
        // Initialize the database
        scheduleRecordDetailsRepository.saveAndFlush(scheduleRecordDetails);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();

        // Update the scheduleRecordDetails
        ScheduleRecordDetails updatedScheduleRecordDetails = scheduleRecordDetailsRepository.findById(scheduleRecordDetails.getId()).get();
        // Disconnect from session so that the updates on updatedScheduleRecordDetails are not directly saved in db
        em.detach(updatedScheduleRecordDetails);
        updatedScheduleRecordDetails
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);

        restScheduleRecordDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduleRecordDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheduleRecordDetails))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetails testScheduleRecordDetails = scheduleRecordDetailsList.get(scheduleRecordDetailsList.size() - 1);
        assertThat(testScheduleRecordDetails.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetails.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordDetails.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetails.getdNum()).isEqualTo(UPDATED_D_NUM);
        assertThat(testScheduleRecordDetails.getIdNum()).isEqualTo(UPDATED_ID_NUM);
    }

    @Test
    @Transactional
    void putNonExistingScheduleRecordDetails() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();
        scheduleRecordDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleRecordDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleRecordDetails() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();
        scheduleRecordDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleRecordDetails() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();
        scheduleRecordDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleRecordDetailsWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordDetailsRepository.saveAndFlush(scheduleRecordDetails);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();

        // Update the scheduleRecordDetails using partial update
        ScheduleRecordDetails partialUpdatedScheduleRecordDetails = new ScheduleRecordDetails();
        partialUpdatedScheduleRecordDetails.setId(scheduleRecordDetails.getId());

        partialUpdatedScheduleRecordDetails.dName(UPDATED_D_NAME);

        restScheduleRecordDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordDetails))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetails testScheduleRecordDetails = scheduleRecordDetailsList.get(scheduleRecordDetailsList.size() - 1);
        assertThat(testScheduleRecordDetails.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetails.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordDetails.getMedicalNumber()).isEqualTo(DEFAULT_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetails.getdNum()).isEqualTo(DEFAULT_D_NUM);
        assertThat(testScheduleRecordDetails.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
    }

    @Test
    @Transactional
    void fullUpdateScheduleRecordDetailsWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordDetailsRepository.saveAndFlush(scheduleRecordDetails);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();

        // Update the scheduleRecordDetails using partial update
        ScheduleRecordDetails partialUpdatedScheduleRecordDetails = new ScheduleRecordDetails();
        partialUpdatedScheduleRecordDetails.setId(scheduleRecordDetails.getId());

        partialUpdatedScheduleRecordDetails
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);

        restScheduleRecordDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordDetails))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetails testScheduleRecordDetails = scheduleRecordDetailsList.get(scheduleRecordDetailsList.size() - 1);
        assertThat(testScheduleRecordDetails.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetails.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordDetails.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetails.getdNum()).isEqualTo(UPDATED_D_NUM);
        assertThat(testScheduleRecordDetails.getIdNum()).isEqualTo(UPDATED_ID_NUM);
    }

    @Test
    @Transactional
    void patchNonExistingScheduleRecordDetails() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();
        scheduleRecordDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleRecordDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleRecordDetails() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();
        scheduleRecordDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleRecordDetails() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsRepository.findAll().size();
        scheduleRecordDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordDetails in the database
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleRecordDetails() throws Exception {
        // Initialize the database
        scheduleRecordDetailsRepository.saveAndFlush(scheduleRecordDetails);

        int databaseSizeBeforeDelete = scheduleRecordDetailsRepository.findAll().size();

        // Delete the scheduleRecordDetails
        restScheduleRecordDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleRecordDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduleRecordDetails> scheduleRecordDetailsList = scheduleRecordDetailsRepository.findAll();
        assertThat(scheduleRecordDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
