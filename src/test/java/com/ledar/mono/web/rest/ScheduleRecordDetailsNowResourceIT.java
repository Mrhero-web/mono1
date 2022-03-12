package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ScheduleRecordDetailsNow;
import com.ledar.mono.repository.ScheduleRecordDetailsNowRepository;
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
 * Integration tests for the {@link ScheduleRecordDetailsNowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleRecordDetailsNowResourceIT {

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

    private static final String ENTITY_API_URL = "/api/schedule-record-details-nows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduleRecordDetailsNowRepository scheduleRecordDetailsNowRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleRecordDetailsNowMockMvc;

    private ScheduleRecordDetailsNow scheduleRecordDetailsNow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordDetailsNow createEntity(EntityManager em) {
        ScheduleRecordDetailsNow scheduleRecordDetailsNow = new ScheduleRecordDetailsNow()
            .dName(DEFAULT_D_NAME)
            .cureId(DEFAULT_CURE_ID)
            .medicalNumber(DEFAULT_MEDICAL_NUMBER)
            .dNum(DEFAULT_D_NUM)
            .idNum(DEFAULT_ID_NUM);
        return scheduleRecordDetailsNow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordDetailsNow createUpdatedEntity(EntityManager em) {
        ScheduleRecordDetailsNow scheduleRecordDetailsNow = new ScheduleRecordDetailsNow()
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);
        return scheduleRecordDetailsNow;
    }

    @BeforeEach
    public void initTest() {
        scheduleRecordDetailsNow = createEntity(em);
    }

    @Test
    @Transactional
    void createScheduleRecordDetailsNow() throws Exception {
        int databaseSizeBeforeCreate = scheduleRecordDetailsNowRepository.findAll().size();
        // Create the ScheduleRecordDetailsNow
        restScheduleRecordDetailsNowMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isCreated());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleRecordDetailsNow testScheduleRecordDetailsNow = scheduleRecordDetailsNowList.get(scheduleRecordDetailsNowList.size() - 1);
        assertThat(testScheduleRecordDetailsNow.getdName()).isEqualTo(DEFAULT_D_NAME);
        assertThat(testScheduleRecordDetailsNow.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordDetailsNow.getMedicalNumber()).isEqualTo(DEFAULT_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsNow.getdNum()).isEqualTo(DEFAULT_D_NUM);
        assertThat(testScheduleRecordDetailsNow.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
    }

    @Test
    @Transactional
    void createScheduleRecordDetailsNowWithExistingId() throws Exception {
        // Create the ScheduleRecordDetailsNow with an existing ID
        scheduleRecordDetailsNow.setId(1L);

        int databaseSizeBeforeCreate = scheduleRecordDetailsNowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleRecordDetailsNowMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScheduleRecordDetailsNows() throws Exception {
        // Initialize the database
        scheduleRecordDetailsNowRepository.saveAndFlush(scheduleRecordDetailsNow);

        // Get all the scheduleRecordDetailsNowList
        restScheduleRecordDetailsNowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleRecordDetailsNow.getId().intValue())))
            .andExpect(jsonPath("$.[*].dName").value(hasItem(DEFAULT_D_NAME)))
            .andExpect(jsonPath("$.[*].cureId").value(hasItem(DEFAULT_CURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].medicalNumber").value(hasItem(DEFAULT_MEDICAL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].dNum").value(hasItem(DEFAULT_D_NUM.intValue())))
            .andExpect(jsonPath("$.[*].idNum").value(hasItem(DEFAULT_ID_NUM)));
    }

    @Test
    @Transactional
    void getScheduleRecordDetailsNow() throws Exception {
        // Initialize the database
        scheduleRecordDetailsNowRepository.saveAndFlush(scheduleRecordDetailsNow);

        // Get the scheduleRecordDetailsNow
        restScheduleRecordDetailsNowMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleRecordDetailsNow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleRecordDetailsNow.getId().intValue()))
            .andExpect(jsonPath("$.dName").value(DEFAULT_D_NAME))
            .andExpect(jsonPath("$.cureId").value(DEFAULT_CURE_ID.intValue()))
            .andExpect(jsonPath("$.medicalNumber").value(DEFAULT_MEDICAL_NUMBER.intValue()))
            .andExpect(jsonPath("$.dNum").value(DEFAULT_D_NUM.intValue()))
            .andExpect(jsonPath("$.idNum").value(DEFAULT_ID_NUM));
    }

    @Test
    @Transactional
    void getNonExistingScheduleRecordDetailsNow() throws Exception {
        // Get the scheduleRecordDetailsNow
        restScheduleRecordDetailsNowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheduleRecordDetailsNow() throws Exception {
        // Initialize the database
        scheduleRecordDetailsNowRepository.saveAndFlush(scheduleRecordDetailsNow);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();

        // Update the scheduleRecordDetailsNow
        ScheduleRecordDetailsNow updatedScheduleRecordDetailsNow = scheduleRecordDetailsNowRepository
            .findById(scheduleRecordDetailsNow.getId())
            .get();
        // Disconnect from session so that the updates on updatedScheduleRecordDetailsNow are not directly saved in db
        em.detach(updatedScheduleRecordDetailsNow);
        updatedScheduleRecordDetailsNow
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);

        restScheduleRecordDetailsNowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduleRecordDetailsNow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheduleRecordDetailsNow))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetailsNow testScheduleRecordDetailsNow = scheduleRecordDetailsNowList.get(scheduleRecordDetailsNowList.size() - 1);
        assertThat(testScheduleRecordDetailsNow.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetailsNow.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordDetailsNow.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsNow.getdNum()).isEqualTo(UPDATED_D_NUM);
        assertThat(testScheduleRecordDetailsNow.getIdNum()).isEqualTo(UPDATED_ID_NUM);
    }

    @Test
    @Transactional
    void putNonExistingScheduleRecordDetailsNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();
        scheduleRecordDetailsNow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsNowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleRecordDetailsNow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleRecordDetailsNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();
        scheduleRecordDetailsNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsNowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleRecordDetailsNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();
        scheduleRecordDetailsNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsNowMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleRecordDetailsNowWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordDetailsNowRepository.saveAndFlush(scheduleRecordDetailsNow);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();

        // Update the scheduleRecordDetailsNow using partial update
        ScheduleRecordDetailsNow partialUpdatedScheduleRecordDetailsNow = new ScheduleRecordDetailsNow();
        partialUpdatedScheduleRecordDetailsNow.setId(scheduleRecordDetailsNow.getId());

        partialUpdatedScheduleRecordDetailsNow.dName(UPDATED_D_NAME).medicalNumber(UPDATED_MEDICAL_NUMBER).dNum(UPDATED_D_NUM);

        restScheduleRecordDetailsNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordDetailsNow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordDetailsNow))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetailsNow testScheduleRecordDetailsNow = scheduleRecordDetailsNowList.get(scheduleRecordDetailsNowList.size() - 1);
        assertThat(testScheduleRecordDetailsNow.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetailsNow.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordDetailsNow.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsNow.getdNum()).isEqualTo(UPDATED_D_NUM);
        assertThat(testScheduleRecordDetailsNow.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
    }

    @Test
    @Transactional
    void fullUpdateScheduleRecordDetailsNowWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordDetailsNowRepository.saveAndFlush(scheduleRecordDetailsNow);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();

        // Update the scheduleRecordDetailsNow using partial update
        ScheduleRecordDetailsNow partialUpdatedScheduleRecordDetailsNow = new ScheduleRecordDetailsNow();
        partialUpdatedScheduleRecordDetailsNow.setId(scheduleRecordDetailsNow.getId());

        partialUpdatedScheduleRecordDetailsNow
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);

        restScheduleRecordDetailsNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordDetailsNow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordDetailsNow))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetailsNow testScheduleRecordDetailsNow = scheduleRecordDetailsNowList.get(scheduleRecordDetailsNowList.size() - 1);
        assertThat(testScheduleRecordDetailsNow.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetailsNow.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordDetailsNow.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsNow.getdNum()).isEqualTo(UPDATED_D_NUM);
        assertThat(testScheduleRecordDetailsNow.getIdNum()).isEqualTo(UPDATED_ID_NUM);
    }

    @Test
    @Transactional
    void patchNonExistingScheduleRecordDetailsNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();
        scheduleRecordDetailsNow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleRecordDetailsNow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleRecordDetailsNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();
        scheduleRecordDetailsNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleRecordDetailsNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsNowRepository.findAll().size();
        scheduleRecordDetailsNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsNowMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsNow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordDetailsNow in the database
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleRecordDetailsNow() throws Exception {
        // Initialize the database
        scheduleRecordDetailsNowRepository.saveAndFlush(scheduleRecordDetailsNow);

        int databaseSizeBeforeDelete = scheduleRecordDetailsNowRepository.findAll().size();

        // Delete the scheduleRecordDetailsNow
        restScheduleRecordDetailsNowMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleRecordDetailsNow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduleRecordDetailsNow> scheduleRecordDetailsNowList = scheduleRecordDetailsNowRepository.findAll();
        assertThat(scheduleRecordDetailsNowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
