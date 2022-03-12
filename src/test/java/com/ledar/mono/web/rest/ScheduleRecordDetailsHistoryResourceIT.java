package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ScheduleRecordDetailsHistory;
import com.ledar.mono.repository.ScheduleRecordDetailsHistoryRepository;
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
 * Integration tests for the {@link ScheduleRecordDetailsHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleRecordDetailsHistoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/schedule-record-details-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScheduleRecordDetailsHistoryRepository scheduleRecordDetailsHistoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleRecordDetailsHistoryMockMvc;

    private ScheduleRecordDetailsHistory scheduleRecordDetailsHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordDetailsHistory createEntity(EntityManager em) {
        ScheduleRecordDetailsHistory scheduleRecordDetailsHistory = new ScheduleRecordDetailsHistory()
            .dName(DEFAULT_D_NAME)
            .cureId(DEFAULT_CURE_ID)
            .medicalNumber(DEFAULT_MEDICAL_NUMBER)
            .dNum(DEFAULT_D_NUM)
            .idNum(DEFAULT_ID_NUM);
        return scheduleRecordDetailsHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleRecordDetailsHistory createUpdatedEntity(EntityManager em) {
        ScheduleRecordDetailsHistory scheduleRecordDetailsHistory = new ScheduleRecordDetailsHistory()
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);
        return scheduleRecordDetailsHistory;
    }

    @BeforeEach
    public void initTest() {
        scheduleRecordDetailsHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createScheduleRecordDetailsHistory() throws Exception {
        int databaseSizeBeforeCreate = scheduleRecordDetailsHistoryRepository.findAll().size();
        // Create the ScheduleRecordDetailsHistory
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isCreated());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduleRecordDetailsHistory testScheduleRecordDetailsHistory = scheduleRecordDetailsHistoryList.get(
            scheduleRecordDetailsHistoryList.size() - 1
        );
        assertThat(testScheduleRecordDetailsHistory.getdName()).isEqualTo(DEFAULT_D_NAME);
        assertThat(testScheduleRecordDetailsHistory.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordDetailsHistory.getMedicalNumber()).isEqualTo(DEFAULT_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsHistory.getdNum()).isEqualTo(DEFAULT_D_NUM);
        assertThat(testScheduleRecordDetailsHistory.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
    }

    @Test
    @Transactional
    void createScheduleRecordDetailsHistoryWithExistingId() throws Exception {
        // Create the ScheduleRecordDetailsHistory with an existing ID
        scheduleRecordDetailsHistory.setId(1L);

        int databaseSizeBeforeCreate = scheduleRecordDetailsHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScheduleRecordDetailsHistories() throws Exception {
        // Initialize the database
        scheduleRecordDetailsHistoryRepository.saveAndFlush(scheduleRecordDetailsHistory);

        // Get all the scheduleRecordDetailsHistoryList
        restScheduleRecordDetailsHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleRecordDetailsHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].dName").value(hasItem(DEFAULT_D_NAME)))
            .andExpect(jsonPath("$.[*].cureId").value(hasItem(DEFAULT_CURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].medicalNumber").value(hasItem(DEFAULT_MEDICAL_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].dNum").value(hasItem(DEFAULT_D_NUM.intValue())))
            .andExpect(jsonPath("$.[*].idNum").value(hasItem(DEFAULT_ID_NUM)));
    }

    @Test
    @Transactional
    void getScheduleRecordDetailsHistory() throws Exception {
        // Initialize the database
        scheduleRecordDetailsHistoryRepository.saveAndFlush(scheduleRecordDetailsHistory);

        // Get the scheduleRecordDetailsHistory
        restScheduleRecordDetailsHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleRecordDetailsHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleRecordDetailsHistory.getId().intValue()))
            .andExpect(jsonPath("$.dName").value(DEFAULT_D_NAME))
            .andExpect(jsonPath("$.cureId").value(DEFAULT_CURE_ID.intValue()))
            .andExpect(jsonPath("$.medicalNumber").value(DEFAULT_MEDICAL_NUMBER.intValue()))
            .andExpect(jsonPath("$.dNum").value(DEFAULT_D_NUM.intValue()))
            .andExpect(jsonPath("$.idNum").value(DEFAULT_ID_NUM));
    }

    @Test
    @Transactional
    void getNonExistingScheduleRecordDetailsHistory() throws Exception {
        // Get the scheduleRecordDetailsHistory
        restScheduleRecordDetailsHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScheduleRecordDetailsHistory() throws Exception {
        // Initialize the database
        scheduleRecordDetailsHistoryRepository.saveAndFlush(scheduleRecordDetailsHistory);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();

        // Update the scheduleRecordDetailsHistory
        ScheduleRecordDetailsHistory updatedScheduleRecordDetailsHistory = scheduleRecordDetailsHistoryRepository
            .findById(scheduleRecordDetailsHistory.getId())
            .get();
        // Disconnect from session so that the updates on updatedScheduleRecordDetailsHistory are not directly saved in db
        em.detach(updatedScheduleRecordDetailsHistory);
        updatedScheduleRecordDetailsHistory
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);

        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScheduleRecordDetailsHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScheduleRecordDetailsHistory))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetailsHistory testScheduleRecordDetailsHistory = scheduleRecordDetailsHistoryList.get(
            scheduleRecordDetailsHistoryList.size() - 1
        );
        assertThat(testScheduleRecordDetailsHistory.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetailsHistory.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordDetailsHistory.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsHistory.getdNum()).isEqualTo(UPDATED_D_NUM);
        assertThat(testScheduleRecordDetailsHistory.getIdNum()).isEqualTo(UPDATED_ID_NUM);
    }

    @Test
    @Transactional
    void putNonExistingScheduleRecordDetailsHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();
        scheduleRecordDetailsHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleRecordDetailsHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleRecordDetailsHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();
        scheduleRecordDetailsHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleRecordDetailsHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();
        scheduleRecordDetailsHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleRecordDetailsHistoryWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordDetailsHistoryRepository.saveAndFlush(scheduleRecordDetailsHistory);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();

        // Update the scheduleRecordDetailsHistory using partial update
        ScheduleRecordDetailsHistory partialUpdatedScheduleRecordDetailsHistory = new ScheduleRecordDetailsHistory();
        partialUpdatedScheduleRecordDetailsHistory.setId(scheduleRecordDetailsHistory.getId());

        partialUpdatedScheduleRecordDetailsHistory.medicalNumber(UPDATED_MEDICAL_NUMBER);

        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordDetailsHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordDetailsHistory))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetailsHistory testScheduleRecordDetailsHistory = scheduleRecordDetailsHistoryList.get(
            scheduleRecordDetailsHistoryList.size() - 1
        );
        assertThat(testScheduleRecordDetailsHistory.getdName()).isEqualTo(DEFAULT_D_NAME);
        assertThat(testScheduleRecordDetailsHistory.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testScheduleRecordDetailsHistory.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsHistory.getdNum()).isEqualTo(DEFAULT_D_NUM);
        assertThat(testScheduleRecordDetailsHistory.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
    }

    @Test
    @Transactional
    void fullUpdateScheduleRecordDetailsHistoryWithPatch() throws Exception {
        // Initialize the database
        scheduleRecordDetailsHistoryRepository.saveAndFlush(scheduleRecordDetailsHistory);

        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();

        // Update the scheduleRecordDetailsHistory using partial update
        ScheduleRecordDetailsHistory partialUpdatedScheduleRecordDetailsHistory = new ScheduleRecordDetailsHistory();
        partialUpdatedScheduleRecordDetailsHistory.setId(scheduleRecordDetailsHistory.getId());

        partialUpdatedScheduleRecordDetailsHistory
            .dName(UPDATED_D_NAME)
            .cureId(UPDATED_CURE_ID)
            .medicalNumber(UPDATED_MEDICAL_NUMBER)
            .dNum(UPDATED_D_NUM)
            .idNum(UPDATED_ID_NUM);

        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleRecordDetailsHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScheduleRecordDetailsHistory))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
        ScheduleRecordDetailsHistory testScheduleRecordDetailsHistory = scheduleRecordDetailsHistoryList.get(
            scheduleRecordDetailsHistoryList.size() - 1
        );
        assertThat(testScheduleRecordDetailsHistory.getdName()).isEqualTo(UPDATED_D_NAME);
        assertThat(testScheduleRecordDetailsHistory.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testScheduleRecordDetailsHistory.getMedicalNumber()).isEqualTo(UPDATED_MEDICAL_NUMBER);
        assertThat(testScheduleRecordDetailsHistory.getdNum()).isEqualTo(UPDATED_D_NUM);
        assertThat(testScheduleRecordDetailsHistory.getIdNum()).isEqualTo(UPDATED_ID_NUM);
    }

    @Test
    @Transactional
    void patchNonExistingScheduleRecordDetailsHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();
        scheduleRecordDetailsHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleRecordDetailsHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleRecordDetailsHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();
        scheduleRecordDetailsHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleRecordDetailsHistory() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRecordDetailsHistoryRepository.findAll().size();
        scheduleRecordDetailsHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleRecordDetailsHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(scheduleRecordDetailsHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleRecordDetailsHistory in the database
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleRecordDetailsHistory() throws Exception {
        // Initialize the database
        scheduleRecordDetailsHistoryRepository.saveAndFlush(scheduleRecordDetailsHistory);

        int databaseSizeBeforeDelete = scheduleRecordDetailsHistoryRepository.findAll().size();

        // Delete the scheduleRecordDetailsHistory
        restScheduleRecordDetailsHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleRecordDetailsHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ScheduleRecordDetailsHistory> scheduleRecordDetailsHistoryList = scheduleRecordDetailsHistoryRepository.findAll();
        assertThat(scheduleRecordDetailsHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
