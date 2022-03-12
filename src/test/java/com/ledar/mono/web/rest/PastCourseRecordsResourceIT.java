package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.PastCourseRecords;
import com.ledar.mono.domain.enumeration.CourseStatus;
import com.ledar.mono.repository.PastCourseRecordsRepository;
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
 * Integration tests for the {@link PastCourseRecordsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PastCourseRecordsResourceIT {

    private static final Long DEFAULT_C_ID = 1L;
    private static final Long UPDATED_C_ID = 2L;

    private static final Long DEFAULT_T_ID = 1L;
    private static final Long UPDATED_T_ID = 2L;

    private static final Long DEFAULT_R_ID = 1L;
    private static final Long UPDATED_R_ID = 2L;

    private static final Instant DEFAULT_CLASS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLASS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SCHOOL_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHOOL_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLASS_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLASS_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CourseStatus DEFAULT_C_STATUS = CourseStatus.NOTSTART;
    private static final CourseStatus UPDATED_C_STATUS = CourseStatus.COMPLETE;

    private static final String ENTITY_API_URL = "/api/past-course-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PastCourseRecordsRepository pastCourseRecordsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPastCourseRecordsMockMvc;

    private PastCourseRecords pastCourseRecords;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PastCourseRecords createEntity(EntityManager em) {
        PastCourseRecords pastCourseRecords = new PastCourseRecords()
            .cId(DEFAULT_C_ID)
            .tId(DEFAULT_T_ID)
            .rId(DEFAULT_R_ID)
            .classDate(DEFAULT_CLASS_DATE)
            .schoolTime(DEFAULT_SCHOOL_TIME)
            .classTime(DEFAULT_CLASS_TIME)
            .cStatus(DEFAULT_C_STATUS);
        return pastCourseRecords;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PastCourseRecords createUpdatedEntity(EntityManager em) {
        PastCourseRecords pastCourseRecords = new PastCourseRecords()
            .cId(UPDATED_C_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .cStatus(UPDATED_C_STATUS);
        return pastCourseRecords;
    }

    @BeforeEach
    public void initTest() {
        pastCourseRecords = createEntity(em);
    }

    @Test
    @Transactional
    void createPastCourseRecords() throws Exception {
        int databaseSizeBeforeCreate = pastCourseRecordsRepository.findAll().size();
        // Create the PastCourseRecords
        restPastCourseRecordsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isCreated());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeCreate + 1);
        PastCourseRecords testPastCourseRecords = pastCourseRecordsList.get(pastCourseRecordsList.size() - 1);
        assertThat(testPastCourseRecords.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testPastCourseRecords.gettId()).isEqualTo(DEFAULT_T_ID);
        assertThat(testPastCourseRecords.getrId()).isEqualTo(DEFAULT_R_ID);
        assertThat(testPastCourseRecords.getClassDate()).isEqualTo(DEFAULT_CLASS_DATE);
        assertThat(testPastCourseRecords.getSchoolTime()).isEqualTo(DEFAULT_SCHOOL_TIME);
        assertThat(testPastCourseRecords.getClassTime()).isEqualTo(DEFAULT_CLASS_TIME);
        assertThat(testPastCourseRecords.getcStatus()).isEqualTo(DEFAULT_C_STATUS);
    }

    @Test
    @Transactional
    void createPastCourseRecordsWithExistingId() throws Exception {
        // Create the PastCourseRecords with an existing ID
        pastCourseRecords.setId(1L);

        int databaseSizeBeforeCreate = pastCourseRecordsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPastCourseRecordsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPastCourseRecords() throws Exception {
        // Initialize the database
        pastCourseRecordsRepository.saveAndFlush(pastCourseRecords);

        // Get all the pastCourseRecordsList
        restPastCourseRecordsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pastCourseRecords.getId().intValue())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.intValue())))
            .andExpect(jsonPath("$.[*].tId").value(hasItem(DEFAULT_T_ID.intValue())))
            .andExpect(jsonPath("$.[*].rId").value(hasItem(DEFAULT_R_ID.intValue())))
            .andExpect(jsonPath("$.[*].classDate").value(hasItem(DEFAULT_CLASS_DATE.toString())))
            .andExpect(jsonPath("$.[*].schoolTime").value(hasItem(DEFAULT_SCHOOL_TIME.toString())))
            .andExpect(jsonPath("$.[*].classTime").value(hasItem(DEFAULT_CLASS_TIME.toString())))
            .andExpect(jsonPath("$.[*].cStatus").value(hasItem(DEFAULT_C_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPastCourseRecords() throws Exception {
        // Initialize the database
        pastCourseRecordsRepository.saveAndFlush(pastCourseRecords);

        // Get the pastCourseRecords
        restPastCourseRecordsMockMvc
            .perform(get(ENTITY_API_URL_ID, pastCourseRecords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pastCourseRecords.getId().intValue()))
            .andExpect(jsonPath("$.cId").value(DEFAULT_C_ID.intValue()))
            .andExpect(jsonPath("$.tId").value(DEFAULT_T_ID.intValue()))
            .andExpect(jsonPath("$.rId").value(DEFAULT_R_ID.intValue()))
            .andExpect(jsonPath("$.classDate").value(DEFAULT_CLASS_DATE.toString()))
            .andExpect(jsonPath("$.schoolTime").value(DEFAULT_SCHOOL_TIME.toString()))
            .andExpect(jsonPath("$.classTime").value(DEFAULT_CLASS_TIME.toString()))
            .andExpect(jsonPath("$.cStatus").value(DEFAULT_C_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPastCourseRecords() throws Exception {
        // Get the pastCourseRecords
        restPastCourseRecordsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPastCourseRecords() throws Exception {
        // Initialize the database
        pastCourseRecordsRepository.saveAndFlush(pastCourseRecords);

        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();

        // Update the pastCourseRecords
        PastCourseRecords updatedPastCourseRecords = pastCourseRecordsRepository.findById(pastCourseRecords.getId()).get();
        // Disconnect from session so that the updates on updatedPastCourseRecords are not directly saved in db
        em.detach(updatedPastCourseRecords);
        updatedPastCourseRecords
            .cId(UPDATED_C_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .cStatus(UPDATED_C_STATUS);

        restPastCourseRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPastCourseRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPastCourseRecords))
            )
            .andExpect(status().isOk());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
        PastCourseRecords testPastCourseRecords = pastCourseRecordsList.get(pastCourseRecordsList.size() - 1);
        assertThat(testPastCourseRecords.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testPastCourseRecords.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testPastCourseRecords.getrId()).isEqualTo(UPDATED_R_ID);
        assertThat(testPastCourseRecords.getClassDate()).isEqualTo(UPDATED_CLASS_DATE);
        assertThat(testPastCourseRecords.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testPastCourseRecords.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testPastCourseRecords.getcStatus()).isEqualTo(UPDATED_C_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingPastCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();
        pastCourseRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPastCourseRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pastCourseRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPastCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();
        pastCourseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastCourseRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPastCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();
        pastCourseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastCourseRecordsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePastCourseRecordsWithPatch() throws Exception {
        // Initialize the database
        pastCourseRecordsRepository.saveAndFlush(pastCourseRecords);

        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();

        // Update the pastCourseRecords using partial update
        PastCourseRecords partialUpdatedPastCourseRecords = new PastCourseRecords();
        partialUpdatedPastCourseRecords.setId(pastCourseRecords.getId());

        partialUpdatedPastCourseRecords
            .tId(UPDATED_T_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME);

        restPastCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPastCourseRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPastCourseRecords))
            )
            .andExpect(status().isOk());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
        PastCourseRecords testPastCourseRecords = pastCourseRecordsList.get(pastCourseRecordsList.size() - 1);
        assertThat(testPastCourseRecords.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testPastCourseRecords.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testPastCourseRecords.getrId()).isEqualTo(DEFAULT_R_ID);
        assertThat(testPastCourseRecords.getClassDate()).isEqualTo(UPDATED_CLASS_DATE);
        assertThat(testPastCourseRecords.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testPastCourseRecords.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testPastCourseRecords.getcStatus()).isEqualTo(DEFAULT_C_STATUS);
    }

    @Test
    @Transactional
    void fullUpdatePastCourseRecordsWithPatch() throws Exception {
        // Initialize the database
        pastCourseRecordsRepository.saveAndFlush(pastCourseRecords);

        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();

        // Update the pastCourseRecords using partial update
        PastCourseRecords partialUpdatedPastCourseRecords = new PastCourseRecords();
        partialUpdatedPastCourseRecords.setId(pastCourseRecords.getId());

        partialUpdatedPastCourseRecords
            .cId(UPDATED_C_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .cStatus(UPDATED_C_STATUS);

        restPastCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPastCourseRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPastCourseRecords))
            )
            .andExpect(status().isOk());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
        PastCourseRecords testPastCourseRecords = pastCourseRecordsList.get(pastCourseRecordsList.size() - 1);
        assertThat(testPastCourseRecords.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testPastCourseRecords.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testPastCourseRecords.getrId()).isEqualTo(UPDATED_R_ID);
        assertThat(testPastCourseRecords.getClassDate()).isEqualTo(UPDATED_CLASS_DATE);
        assertThat(testPastCourseRecords.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testPastCourseRecords.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testPastCourseRecords.getcStatus()).isEqualTo(UPDATED_C_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPastCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();
        pastCourseRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPastCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pastCourseRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPastCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();
        pastCourseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPastCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastCourseRecordsRepository.findAll().size();
        pastCourseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pastCourseRecords))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PastCourseRecords in the database
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePastCourseRecords() throws Exception {
        // Initialize the database
        pastCourseRecordsRepository.saveAndFlush(pastCourseRecords);

        int databaseSizeBeforeDelete = pastCourseRecordsRepository.findAll().size();

        // Delete the pastCourseRecords
        restPastCourseRecordsMockMvc
            .perform(delete(ENTITY_API_URL_ID, pastCourseRecords.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PastCourseRecords> pastCourseRecordsList = pastCourseRecordsRepository.findAll();
        assertThat(pastCourseRecordsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
