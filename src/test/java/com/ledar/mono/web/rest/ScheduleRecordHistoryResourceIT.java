package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ScheduleRecordHistory;
import com.ledar.mono.domain.enumeration.Confirmation;
import com.ledar.mono.repository.ScheduleRecordHistoryRepository;
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
 * Integration tests for the {@link ScheduleRecordHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleRecordHistoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/schedule-record-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduleRecordHistoryRepository scheduleRecordHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleRecordHistoryMockMvc;

    private ScheduleRecordHistory scheduleRecordHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordHistory createEntity(EntityManager em) {
        ScheduleRecordHistory scheduleRecordHistory = new ScheduleRecordHistory()
            .cureProjectNum(DEFAULT_CURE_PROJECT_NUM)
            .cureId(DEFAULT_CURE_ID)
            .name(DEFAULT_NAME)
            .scheduleTime(DEFAULT_SCHEDULE_TIME)
            .scheduleIsachive(DEFAULT_SCHEDULE_ISACHIVE)
            .scheduleCureTime(DEFAULT_SCHEDULE_CURE_TIME)
            .detailsNum(DEFAULT_DETAILS_NUM)
            .photoUrl(DEFAULT_PHOTO_URL);
        return scheduleRecordHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordHistory createUpdatedEntity(EntityManager em) {
        ScheduleRecordHistory scheduleRecordHistory = new ScheduleRecordHistory()
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);
        return scheduleRecordHistory;
    }

    @BeforeEach
    public void initTest() {
        scheduleRecordHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createScheduleRecordHistory() throws Exception {
        int databaseSizeBeforeCreate = scheduleRecordHistoryRepository.findAll().size();
        // Create the ScheduleRecordHistory
        restScheduleRecordHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isCreated());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleRecordHistory testScheduleRecordHistory = scheduleRecordHistoryList.get(scheduleRecordHistoryList.size() - 1);
        assertThat(testScheduleRecordHistory.getCureProjectNum()).isEqualTo(DEFAULT_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordHistory.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduleRecordHistory.getScheduleTime()).isEqualTo(DEFAULT_SCHEDULE_TIME);
        assertThat(testScheduleRecordHistory.getScheduleIsachive()).isEqualTo(DEFAULT_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordHistory.getScheduleCureTime()).isEqualTo(DEFAULT_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordHistory.getDetailsNum()).isEqualTo(DEFAULT_DETAILS_NUM);
        assertThat(testScheduleRecordHistory.getPhotoUrl()).isEqualTo(DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    void createScheduleRecordHistoryWithExistingId() throws Exception {
        // Create the ScheduleRecordHistory with an existing ID
        scheduleRecordHistory.setId(1L);

        int databaseSizeBeforeCreate = scheduleRecordHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleRecordHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScheduleRecordHistories() throws Exception {
        // Initialize the database
        scheduleRecordHistoryRepository.saveAndFlush(scheduleRecordHistory);

        // Get all the scheduleRecordHistoryList
        restScheduleRecordHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleRecordHistory.getId().intValue())))
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
    void getScheduleRecordHistory() throws Exception {
        // Initialize the database
        scheduleRecordHistoryRepository.saveAndFlush(scheduleRecordHistory);

        // Get the scheduleRecordHistory
        restScheduleRecordHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleRecordHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleRecordHistory.getId().intValue()))
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
    void getNonExistingScheduleRecordHistory() throws Exception {
        // Get the scheduleRecordHistory
        restScheduleRecordHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheduleRecordHistory() throws Exception {
        // Initialize the database
        scheduleRecordHistoryRepository.saveAndFlush(scheduleRecordHistory);

        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();

        // Update the scheduleRecordHistory
        ScheduleRecordHistory updatedScheduleRecordHistory = scheduleRecordHistoryRepository.findById(scheduleRecordHistory.getId()).get();
        // Disconnect from session so that the updates on updatedScheduleRecordHistory are not directly saved in db
        em.detach(updatedScheduleRecordHistory);
        updatedScheduleRecordHistory
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduleRecordHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheduleRecordHistory))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordHistory testScheduleRecordHistory = scheduleRecordHistoryList.get(scheduleRecordHistoryList.size() - 1);
        assertThat(testScheduleRecordHistory.getCureProjectNum()).isEqualTo(UPDATED_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordHistory.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduleRecordHistory.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecordHistory.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordHistory.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordHistory.getDetailsNum()).isEqualTo(UPDATED_DETAILS_NUM);
        assertThat(testScheduleRecordHistory.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void putNonExistingScheduleRecordHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();
        scheduleRecordHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleRecordHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleRecordHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();
        scheduleRecordHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleRecordHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();
        scheduleRecordHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleRecordHistoryWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordHistoryRepository.saveAndFlush(scheduleRecordHistory);

        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();

        // Update the scheduleRecordHistory using partial update
        ScheduleRecordHistory partialUpdatedScheduleRecordHistory = new ScheduleRecordHistory();
        partialUpdatedScheduleRecordHistory.setId(scheduleRecordHistory.getId());

        partialUpdatedScheduleRecordHistory
            .cureId(UPDATED_CURE_ID)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordHistory))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordHistory testScheduleRecordHistory = scheduleRecordHistoryList.get(scheduleRecordHistoryList.size() - 1);
        assertThat(testScheduleRecordHistory.getCureProjectNum()).isEqualTo(DEFAULT_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordHistory.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduleRecordHistory.getScheduleTime()).isEqualTo(DEFAULT_SCHEDULE_TIME);
        assertThat(testScheduleRecordHistory.getScheduleIsachive()).isEqualTo(DEFAULT_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordHistory.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordHistory.getDetailsNum()).isEqualTo(DEFAULT_DETAILS_NUM);
        assertThat(testScheduleRecordHistory.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void fullUpdateScheduleRecordHistoryWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordHistoryRepository.saveAndFlush(scheduleRecordHistory);

        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();

        // Update the scheduleRecordHistory using partial update
        ScheduleRecordHistory partialUpdatedScheduleRecordHistory = new ScheduleRecordHistory();
        partialUpdatedScheduleRecordHistory.setId(scheduleRecordHistory.getId());

        partialUpdatedScheduleRecordHistory
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordHistory))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordHistory testScheduleRecordHistory = scheduleRecordHistoryList.get(scheduleRecordHistoryList.size() - 1);
        assertThat(testScheduleRecordHistory.getCureProjectNum()).isEqualTo(UPDATED_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordHistory.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduleRecordHistory.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecordHistory.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordHistory.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordHistory.getDetailsNum()).isEqualTo(UPDATED_DETAILS_NUM);
        assertThat(testScheduleRecordHistory.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void patchNonExistingScheduleRecordHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();
        scheduleRecordHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleRecordHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleRecordHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();
        scheduleRecordHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleRecordHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordHistoryRepository.findAll().size();
        scheduleRecordHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordHistory in the database
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleRecordHistory() throws Exception {
        // Initialize the database
        scheduleRecordHistoryRepository.saveAndFlush(scheduleRecordHistory);

        int databaseSizeBeforeDelete = scheduleRecordHistoryRepository.findAll().size();

        // Delete the scheduleRecordHistory
        restScheduleRecordHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleRecordHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduleRecordHistory> scheduleRecordHistoryList = scheduleRecordHistoryRepository.findAll();
        assertThat(scheduleRecordHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
