package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ScheduleRecordNow;
import com.ledar.mono.domain.enumeration.Confirmation;
import com.ledar.mono.repository.ScheduleRecordNowRepository;
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
 * Integration tests for the {@link ScheduleRecordNowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleRecordNowResourceIT {

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

    private static final String ENTITY_API_URL = "/api/schedule-record-nows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduleRecordNowRepository scheduleRecordNowRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleRecordNowMockMvc;

    private ScheduleRecordNow scheduleRecordNow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordNow createEntity(EntityManager em) {
        ScheduleRecordNow scheduleRecordNow = new ScheduleRecordNow()
            .cureProjectNum(DEFAULT_CURE_PROJECT_NUM)
            .cureId(DEFAULT_CURE_ID)
            .name(DEFAULT_NAME)
            .scheduleTime(DEFAULT_SCHEDULE_TIME)
            .scheduleIsachive(DEFAULT_SCHEDULE_ISACHIVE)
            .scheduleCureTime(DEFAULT_SCHEDULE_CURE_TIME)
            .detailsNum(DEFAULT_DETAILS_NUM)
            .photoUrl(DEFAULT_PHOTO_URL);
        return scheduleRecordNow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordNow createUpdatedEntity(EntityManager em) {
        ScheduleRecordNow scheduleRecordNow = new ScheduleRecordNow()
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);
        return scheduleRecordNow;
    }

    @BeforeEach
    public void initTest() {
        scheduleRecordNow = createEntity(em);
    }

    @Test
    @Transactional
    void createScheduleRecordNow() throws Exception {
        int databaseSizeBeforeCreate = scheduleRecordNowRepository.findAll().size();
        // Create the ScheduleRecordNow
        restScheduleRecordNowMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isCreated());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleRecordNow testScheduleRecordNow = scheduleRecordNowList.get(scheduleRecordNowList.size() - 1);
        assertThat(testScheduleRecordNow.getCureProjectNum()).isEqualTo(DEFAULT_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordNow.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordNow.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduleRecordNow.getScheduleTime()).isEqualTo(DEFAULT_SCHEDULE_TIME);
        assertThat(testScheduleRecordNow.getScheduleIsachive()).isEqualTo(DEFAULT_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordNow.getScheduleCureTime()).isEqualTo(DEFAULT_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordNow.getDetailsNum()).isEqualTo(DEFAULT_DETAILS_NUM);
        assertThat(testScheduleRecordNow.getPhotoUrl()).isEqualTo(DEFAULT_PHOTO_URL);
    }

    @Test
    @Transactional
    void createScheduleRecordNowWithExistingId() throws Exception {
        // Create the ScheduleRecordNow with an existing ID
        scheduleRecordNow.setId(1L);

        int databaseSizeBeforeCreate = scheduleRecordNowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleRecordNowMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScheduleRecordNows() throws Exception {
        // Initialize the database
        scheduleRecordNowRepository.saveAndFlush(scheduleRecordNow);

        // Get all the scheduleRecordNowList
        restScheduleRecordNowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleRecordNow.getId().intValue())))
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
    void getScheduleRecordNow() throws Exception {
        // Initialize the database
        scheduleRecordNowRepository.saveAndFlush(scheduleRecordNow);

        // Get the scheduleRecordNow
        restScheduleRecordNowMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleRecordNow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleRecordNow.getId().intValue()))
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
    void getNonExistingScheduleRecordNow() throws Exception {
        // Get the scheduleRecordNow
        restScheduleRecordNowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheduleRecordNow() throws Exception {
        // Initialize the database
        scheduleRecordNowRepository.saveAndFlush(scheduleRecordNow);

        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();

        // Update the scheduleRecordNow
        ScheduleRecordNow updatedScheduleRecordNow = scheduleRecordNowRepository.findById(scheduleRecordNow.getId()).get();
        // Disconnect from session so that the updates on updatedScheduleRecordNow are not directly saved in db
        em.detach(updatedScheduleRecordNow);
        updatedScheduleRecordNow
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordNowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduleRecordNow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheduleRecordNow))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordNow testScheduleRecordNow = scheduleRecordNowList.get(scheduleRecordNowList.size() - 1);
        assertThat(testScheduleRecordNow.getCureProjectNum()).isEqualTo(UPDATED_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordNow.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordNow.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduleRecordNow.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecordNow.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordNow.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordNow.getDetailsNum()).isEqualTo(UPDATED_DETAILS_NUM);
        assertThat(testScheduleRecordNow.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void putNonExistingScheduleRecordNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();
        scheduleRecordNow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordNowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleRecordNow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleRecordNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();
        scheduleRecordNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordNowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleRecordNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();
        scheduleRecordNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordNowMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleRecordNowWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordNowRepository.saveAndFlush(scheduleRecordNow);

        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();

        // Update the scheduleRecordNow using partial update
        ScheduleRecordNow partialUpdatedScheduleRecordNow = new ScheduleRecordNow();
        partialUpdatedScheduleRecordNow.setId(scheduleRecordNow.getId());

        partialUpdatedScheduleRecordNow
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordNow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordNow))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordNow testScheduleRecordNow = scheduleRecordNowList.get(scheduleRecordNowList.size() - 1);
        assertThat(testScheduleRecordNow.getCureProjectNum()).isEqualTo(UPDATED_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordNow.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordNow.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduleRecordNow.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecordNow.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordNow.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordNow.getDetailsNum()).isEqualTo(DEFAULT_DETAILS_NUM);
        assertThat(testScheduleRecordNow.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void fullUpdateScheduleRecordNowWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordNowRepository.saveAndFlush(scheduleRecordNow);

        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();

        // Update the scheduleRecordNow using partial update
        ScheduleRecordNow partialUpdatedScheduleRecordNow = new ScheduleRecordNow();
        partialUpdatedScheduleRecordNow.setId(scheduleRecordNow.getId());

        partialUpdatedScheduleRecordNow
            .cureProjectNum(UPDATED_CURE_PROJECT_NUM)
            .cureId(UPDATED_CURE_ID)
            .name(UPDATED_NAME)
            .scheduleTime(UPDATED_SCHEDULE_TIME)
            .scheduleIsachive(UPDATED_SCHEDULE_ISACHIVE)
            .scheduleCureTime(UPDATED_SCHEDULE_CURE_TIME)
            .detailsNum(UPDATED_DETAILS_NUM)
            .photoUrl(UPDATED_PHOTO_URL);

        restScheduleRecordNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordNow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordNow))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordNow testScheduleRecordNow = scheduleRecordNowList.get(scheduleRecordNowList.size() - 1);
        assertThat(testScheduleRecordNow.getCureProjectNum()).isEqualTo(UPDATED_CURE_PROJECT_NUM);
        assertThat(testScheduleRecordNow.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordNow.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduleRecordNow.getScheduleTime()).isEqualTo(UPDATED_SCHEDULE_TIME);
        assertThat(testScheduleRecordNow.getScheduleIsachive()).isEqualTo(UPDATED_SCHEDULE_ISACHIVE);
        assertThat(testScheduleRecordNow.getScheduleCureTime()).isEqualTo(UPDATED_SCHEDULE_CURE_TIME);
        assertThat(testScheduleRecordNow.getDetailsNum()).isEqualTo(UPDATED_DETAILS_NUM);
        assertThat(testScheduleRecordNow.getPhotoUrl()).isEqualTo(UPDATED_PHOTO_URL);
    }

    @Test
    @Transactional
    void patchNonExistingScheduleRecordNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();
        scheduleRecordNow.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleRecordNow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleRecordNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();
        scheduleRecordNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordNowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleRecordNow() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordNowRepository.findAll().size();
        scheduleRecordNow.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordNowMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordNow))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordNow in the database
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleRecordNow() throws Exception {
        // Initialize the database
        scheduleRecordNowRepository.saveAndFlush(scheduleRecordNow);

        int databaseSizeBeforeDelete = scheduleRecordNowRepository.findAll().size();

        // Delete the scheduleRecordNow
        restScheduleRecordNowMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleRecordNow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduleRecordNow> scheduleRecordNowList = scheduleRecordNowRepository.findAll();
        assertThat(scheduleRecordNowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
