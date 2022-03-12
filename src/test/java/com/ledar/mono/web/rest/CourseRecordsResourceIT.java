package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.CourseRecords;
import com.ledar.mono.domain.enumeration.CourseStatus;
import com.ledar.mono.repository.CourseRecordsRepository;
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
 * Integration tests for the {@link CourseRecordsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseRecordsResourceIT {

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

    private static final Boolean DEFAULT_MODIFIED = false;
    private static final Boolean UPDATED_MODIFIED = true;

    private static final String ENTITY_API_URL = "/api/course-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseRecordsRepository courseRecordsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseRecordsMockMvc;

    private CourseRecords courseRecords;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseRecords createEntity(EntityManager em) {
        CourseRecords courseRecords = new CourseRecords()
            .cId(DEFAULT_C_ID)
            .tId(DEFAULT_T_ID)
            .rId(DEFAULT_R_ID)
            .classDate(DEFAULT_CLASS_DATE)
            .schoolTime(DEFAULT_SCHOOL_TIME)
            .classTime(DEFAULT_CLASS_TIME)
            .cStatus(DEFAULT_C_STATUS)
            .modified(DEFAULT_MODIFIED);
        return courseRecords;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseRecords createUpdatedEntity(EntityManager em) {
        CourseRecords courseRecords = new CourseRecords()
            .cId(UPDATED_C_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .cStatus(UPDATED_C_STATUS)
            .modified(UPDATED_MODIFIED);
        return courseRecords;
    }

    @BeforeEach
    public void initTest() {
        courseRecords = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseRecords() throws Exception {
        int databaseSizeBeforeCreate = courseRecordsRepository.findAll().size();
        // Create the CourseRecords
        restCourseRecordsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseRecords)))
            .andExpect(status().isCreated());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeCreate + 1);
        CourseRecords testCourseRecords = courseRecordsList.get(courseRecordsList.size() - 1);
        assertThat(testCourseRecords.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testCourseRecords.gettId()).isEqualTo(DEFAULT_T_ID);
        assertThat(testCourseRecords.getrId()).isEqualTo(DEFAULT_R_ID);
        assertThat(testCourseRecords.getClassDate()).isEqualTo(DEFAULT_CLASS_DATE);
        assertThat(testCourseRecords.getSchoolTime()).isEqualTo(DEFAULT_SCHOOL_TIME);
        assertThat(testCourseRecords.getClassTime()).isEqualTo(DEFAULT_CLASS_TIME);
        assertThat(testCourseRecords.getcStatus()).isEqualTo(DEFAULT_C_STATUS);
        assertThat(testCourseRecords.getModified()).isEqualTo(DEFAULT_MODIFIED);
    }

    @Test
    @Transactional
    void createCourseRecordsWithExistingId() throws Exception {
        // Create the CourseRecords with an existing ID
        courseRecords.setId(1L);

        int databaseSizeBeforeCreate = courseRecordsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseRecordsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseRecords)))
            .andExpect(status().isBadRequest());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCourseRecords() throws Exception {
        // Initialize the database
        courseRecordsRepository.saveAndFlush(courseRecords);

        // Get all the courseRecordsList
        restCourseRecordsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseRecords.getId().intValue())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.intValue())))
            .andExpect(jsonPath("$.[*].tId").value(hasItem(DEFAULT_T_ID.intValue())))
            .andExpect(jsonPath("$.[*].rId").value(hasItem(DEFAULT_R_ID.intValue())))
            .andExpect(jsonPath("$.[*].classDate").value(hasItem(DEFAULT_CLASS_DATE.toString())))
            .andExpect(jsonPath("$.[*].schoolTime").value(hasItem(DEFAULT_SCHOOL_TIME.toString())))
            .andExpect(jsonPath("$.[*].classTime").value(hasItem(DEFAULT_CLASS_TIME.toString())))
            .andExpect(jsonPath("$.[*].cStatus").value(hasItem(DEFAULT_C_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modified").value(hasItem(DEFAULT_MODIFIED.booleanValue())));
    }

    @Test
    @Transactional
    void getCourseRecords() throws Exception {
        // Initialize the database
        courseRecordsRepository.saveAndFlush(courseRecords);

        // Get the courseRecords
        restCourseRecordsMockMvc
            .perform(get(ENTITY_API_URL_ID, courseRecords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseRecords.getId().intValue()))
            .andExpect(jsonPath("$.cId").value(DEFAULT_C_ID.intValue()))
            .andExpect(jsonPath("$.tId").value(DEFAULT_T_ID.intValue()))
            .andExpect(jsonPath("$.rId").value(DEFAULT_R_ID.intValue()))
            .andExpect(jsonPath("$.classDate").value(DEFAULT_CLASS_DATE.toString()))
            .andExpect(jsonPath("$.schoolTime").value(DEFAULT_SCHOOL_TIME.toString()))
            .andExpect(jsonPath("$.classTime").value(DEFAULT_CLASS_TIME.toString()))
            .andExpect(jsonPath("$.cStatus").value(DEFAULT_C_STATUS.toString()))
            .andExpect(jsonPath("$.modified").value(DEFAULT_MODIFIED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCourseRecords() throws Exception {
        // Get the courseRecords
        restCourseRecordsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseRecords() throws Exception {
        // Initialize the database
        courseRecordsRepository.saveAndFlush(courseRecords);

        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();

        // Update the courseRecords
        CourseRecords updatedCourseRecords = courseRecordsRepository.findById(courseRecords.getId()).get();
        // Disconnect from session so that the updates on updatedCourseRecords are not directly saved in db
        em.detach(updatedCourseRecords);
        updatedCourseRecords
            .cId(UPDATED_C_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .cStatus(UPDATED_C_STATUS)
            .modified(UPDATED_MODIFIED);

        restCourseRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseRecords))
            )
            .andExpect(status().isOk());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
        CourseRecords testCourseRecords = courseRecordsList.get(courseRecordsList.size() - 1);
        assertThat(testCourseRecords.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testCourseRecords.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testCourseRecords.getrId()).isEqualTo(UPDATED_R_ID);
        assertThat(testCourseRecords.getClassDate()).isEqualTo(UPDATED_CLASS_DATE);
        assertThat(testCourseRecords.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testCourseRecords.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testCourseRecords.getcStatus()).isEqualTo(UPDATED_C_STATUS);
        assertThat(testCourseRecords.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void putNonExistingCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();
        courseRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();
        courseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();
        courseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseRecordsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseRecords)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseRecordsWithPatch() throws Exception {
        // Initialize the database
        courseRecordsRepository.saveAndFlush(courseRecords);

        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();

        // Update the courseRecords using partial update
        CourseRecords partialUpdatedCourseRecords = new CourseRecords();
        partialUpdatedCourseRecords.setId(courseRecords.getId());

        partialUpdatedCourseRecords
            .rId(UPDATED_R_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .modified(UPDATED_MODIFIED);

        restCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseRecords))
            )
            .andExpect(status().isOk());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
        CourseRecords testCourseRecords = courseRecordsList.get(courseRecordsList.size() - 1);
        assertThat(testCourseRecords.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testCourseRecords.gettId()).isEqualTo(DEFAULT_T_ID);
        assertThat(testCourseRecords.getrId()).isEqualTo(UPDATED_R_ID);
        assertThat(testCourseRecords.getClassDate()).isEqualTo(UPDATED_CLASS_DATE);
        assertThat(testCourseRecords.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testCourseRecords.getClassTime()).isEqualTo(DEFAULT_CLASS_TIME);
        assertThat(testCourseRecords.getcStatus()).isEqualTo(DEFAULT_C_STATUS);
        assertThat(testCourseRecords.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void fullUpdateCourseRecordsWithPatch() throws Exception {
        // Initialize the database
        courseRecordsRepository.saveAndFlush(courseRecords);

        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();

        // Update the courseRecords using partial update
        CourseRecords partialUpdatedCourseRecords = new CourseRecords();
        partialUpdatedCourseRecords.setId(courseRecords.getId());

        partialUpdatedCourseRecords
            .cId(UPDATED_C_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .classDate(UPDATED_CLASS_DATE)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .cStatus(UPDATED_C_STATUS)
            .modified(UPDATED_MODIFIED);

        restCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseRecords))
            )
            .andExpect(status().isOk());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
        CourseRecords testCourseRecords = courseRecordsList.get(courseRecordsList.size() - 1);
        assertThat(testCourseRecords.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testCourseRecords.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testCourseRecords.getrId()).isEqualTo(UPDATED_R_ID);
        assertThat(testCourseRecords.getClassDate()).isEqualTo(UPDATED_CLASS_DATE);
        assertThat(testCourseRecords.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testCourseRecords.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testCourseRecords.getcStatus()).isEqualTo(UPDATED_C_STATUS);
        assertThat(testCourseRecords.getModified()).isEqualTo(UPDATED_MODIFIED);
    }

    @Test
    @Transactional
    void patchNonExistingCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();
        courseRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();
        courseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseRecords() throws Exception {
        int databaseSizeBeforeUpdate = courseRecordsRepository.findAll().size();
        courseRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseRecords))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseRecords in the database
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseRecords() throws Exception {
        // Initialize the database
        courseRecordsRepository.saveAndFlush(courseRecords);

        int databaseSizeBeforeDelete = courseRecordsRepository.findAll().size();

        // Delete the courseRecords
        restCourseRecordsMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseRecords.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseRecords> courseRecordsList = courseRecordsRepository.findAll();
        assertThat(courseRecordsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
