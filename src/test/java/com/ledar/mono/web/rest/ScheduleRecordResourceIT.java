package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ScheduleRecord;
import com.ledar.mono.domain.enumeration.Confirmation;
import com.ledar.mono.repository.ScheduleRecordRepository;
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
 * Integration tests for the {@link ScheduleRecordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleRecordResourceIT {

    private static final Long DEFAULT_CURE_PROJECT_NUM = 1L;
    private static final Long UPDATED_CURE_PROJECT_NUM = 2L;

    private static final Long DEFAULT_CURE_ID = 1L;
    private static final Long UPDATED_CURE_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_SCHEDULE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Confirmation DEFAULT_SCHEDULE_ISACHIVE = Confirmation.DELETE;
    private static final Confirmation UPDATED_SCHEDULE_ISACHIVE = Confirmation.FINISHED;

    private static final Instant DEFAULT_SCHEDULE_CURE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHEDULE_CURE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_DETAILS_NUM = 1L;
    private static final Long UPDATED_DETAILS_NUM = 2L;

    private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
    private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/schedule-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduleRecordRepository scheduleRecordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleRecordMockMvc;

    private ScheduleRecord scheduleRecord;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecord createEntity(EntityManager em) {
        ScheduleRecord scheduleRecord = new ScheduleRecord()
            .cureProjectNum(DEFAULT_CURE_PROJECT_NUM)
            .cureId(DEFAULT_CURE_ID)
            .name(DEFAULT_NAME)
            .scheduleTime(DEFAULT_SCHEDULE_TIME)
            .scheduleIsachive(DEFAULT_SCHEDULE_ISACHIVE)
            .scheduleCureTime(DEFAULT_SCHEDULE_CURE_TIME)
            .detailsNum(DEFAULT_DETAILS_NUM)
            .photoUrl(DEFAULT_PHOTO_URL);
        return scheduleRecord;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecord createUpdatedEntity(EntityManager em) {
        ScheduleRecord scheduleRecord = new ScheduleRecord()
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);
        return scheduleRecord;
    }

    @BeforeEach
    public void initTest() {
        scheduleRecord = createEntity(em);
    }

    @Test
    @Transactional
    void createScheduleRecord() throws Exception {
        int databaseSizeBeforeCreate = scheduleRecordRepository.findAll().size();
        // Create the ScheduleRecord
        restScheduleRecordMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleRecord))
            )
            .andExpect(status().isCreated());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleRecord testScheduleRecord = scheduleRecordList.get(scheduleRecordList.size() - 1);
        assertThat(testScheduleRecord.getCureProjectNum()).isEqualTo(DEFAULT_CURE_PROJECT_NUM);
        assertThat(testScheduleRecord.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduleRecord.getScheduleTime()).isEqualTo(DEFAULT_SCHEDULE_TIME);
        assertThat(testScheduleRecord.getScheduleIsachive()).isEqualTo(DEFAULT_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecord.getScheduleCureTime()).isEqualTo(DEFAULT_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecord.getDetailsNum()).isEqualTo(DEFAULT_DETAILS_NUM);
        assertThat(testScheduleRecord.getPhotoUrl()).isEqualTo(DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    void createScheduleRecordWithExistingId() throws Exception {
        // Create the ScheduleRecord with an existing ID
        scheduleRecord.setId(1L);

        int databaseSizeBeforeCreate = scheduleRecordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleRecordMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScheduleRecords() throws Exception {
        // Initialize the database
        scheduleRecordRepository.saveAndFlush(scheduleRecord);

        // Get all the scheduleRecordList
        restScheduleRecordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].cureProjectNum").value(hasItem(DEFAULT_CURE_PROJECT_NUM.intValue())))
            .andExpect(jsonPath("$.[*].cureId").value(hasItem(DEFAULT_CURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scheduleTime").value(hasItem(DEFAULT_SCHEDULE_TIME.toString())))
            .andExpect(jsonPath("$.[*].scheduleIsachive").value(hasItem(DEFAULT_SCHEDULE_ISACHIVE.toString())))
            .andExpect(jsonPath("$.[*].scheduleCureTime").value(hasItem(DEFAULT_SCHEDULE_CURE_TIME.toString())))
            .andExpect(jsonPath("$.[*].detailsNum").value(hasItem(DEFAULT_DETAILS_NUM.intValue())))
            .andExpect(jsonPath("$.[*].photoUrl").value(hasItem(DEFAULT_PHOTO_URL)));
    }

    @Test
    @Transactional
    void getScheduleRecord() throws Exception {
        // Initialize the database
        scheduleRecordRepository.saveAndFlush(scheduleRecord);

        // Get the scheduleRecord
        restScheduleRecordMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleRecord.getId().intValue()))
            .andExpect(jsonPath("$.cureProjectNum").value(DEFAULT_CURE_PROJECT_NUM.intValue()))
            .andExpect(jsonPath("$.cureId").value(DEFAULT_CURE_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.scheduleTime").value(DEFAULT_SCHEDULE_TIME.toString()))
            .andExpect(jsonPath("$.scheduleIsachive").value(DEFAULT_SCHEDULE_ISACHIVE.toString()))
            .andExpect(jsonPath("$.scheduleCureTime").value(DEFAULT_SCHEDULE_CURE_TIME.toString()))
            .andExpect(jsonPath("$.detailsNum").value(DEFAULT_DETAILS_NUM.intValue()))
            .andExpect(jsonPath("$.photoUrl").value(DEFAULT_PHOTO_URL));
    }

    @Test
    @Transactional
    void getNonExistingScheduleRecord() throws Exception {
        // Get the scheduleRecord
        restScheduleRecordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheduleRecord() throws Exception {
        // Initialize the database
        scheduleRecordRepository.saveAndFlush(scheduleRecord);

        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();

        // Update the scheduleRecord
        ScheduleRecord updatedScheduleRecord = scheduleRecordRepository.findById(scheduleRecord.getId()).get();
        // Disconnect from session so that the updates on updatedScheduleRecord are not directly saved in db
        em.detach(updatedScheduleRecord);
        updatedScheduleRecord
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduleRecord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheduleRecord))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecord testScheduleRecord = scheduleRecordList.get(scheduleRecordList.size() - 1);
        assertThat(testScheduleRecord.getCureProjectNum()).isEqualTo(UPDATED_CURE_PROJECT_NUM);
        assertThat(testScheduleRecord.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduleRecord.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecord.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecord.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecord.getDetailsNum()).isEqualTo(UPDATED_DETAILS_NUM);
        assertThat(testScheduleRecord.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void putNonExistingScheduleRecord() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();
        scheduleRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleRecord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleRecord() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();
        scheduleRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleRecord() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();
        scheduleRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleRecord)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleRecordWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordRepository.saveAndFlush(scheduleRecord);

        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();

        // Update the scheduleRecord using partial update
        ScheduleRecord partialUpdatedScheduleRecord = new ScheduleRecord();
        partialUpdatedScheduleRecord.setId(scheduleRecord.getId());

        partialUpdatedScheduleRecord
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecord))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecord testScheduleRecord = scheduleRecordList.get(scheduleRecordList.size() - 1);
        assertThat(testScheduleRecord.getCureProjectNum()).isEqualTo(DEFAULT_CURE_PROJECT_NUM);
        assertThat(testScheduleRecord.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecord.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduleRecord.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecord.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecord.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecord.getDetailsNum()).isEqualTo(UPDATED_DETAILS_NUM);
        assertThat(testScheduleRecord.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void fullUpdateScheduleRecordWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordRepository.saveAndFlush(scheduleRecord);

        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();

        // Update the scheduleRecord using partial update
        ScheduleRecord partialUpdatedScheduleRecord = new ScheduleRecord();
        partialUpdatedScheduleRecord.setId(scheduleRecord.getId());

        partialUpdatedScheduleRecord
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecord))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecord testScheduleRecord = scheduleRecordList.get(scheduleRecordList.size() - 1);
        assertThat(testScheduleRecord.getCureProjectNum()).isEqualTo(UPDATED_CURE_PROJECT_NUM);
        assertThat(testScheduleRecord.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecord.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduleRecord.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecord.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecord.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecord.getDetailsNum()).isEqualTo(UPDATED_DETAILS_NUM);
        assertThat(testScheduleRecord.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void patchNonExistingScheduleRecord() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();
        scheduleRecord.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleRecord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleRecord() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();
        scheduleRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecord))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleRecord() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordRepository.findAll().size();
        scheduleRecord.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(scheduleRecord))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecord in the database
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleRecord() throws Exception {
        // Initialize the database
        scheduleRecordRepository.saveAndFlush(scheduleRecord);

        int databaseSizeBeforeDelete = scheduleRecordRepository.findAll().size();

        // Delete the scheduleRecord
        restScheduleRecordMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleRecord.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduleRecord> scheduleRecordList = scheduleRecordRepository.findAll();
        assertThat(scheduleRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
