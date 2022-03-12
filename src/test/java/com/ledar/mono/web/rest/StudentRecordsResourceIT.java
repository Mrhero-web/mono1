package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.StudentRecords;
import com.ledar.mono.domain.enumeration.SignInStatus;
import com.ledar.mono.repository.StudentRecordsRepository;
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
 * Integration tests for the {@link StudentRecordsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudentRecordsResourceIT {

    private static final Long DEFAULT_CR_ID = 1L;
    private static final Long UPDATED_CR_ID = 2L;

    private static final Long DEFAULT_P_ID = 1L;
    private static final Long UPDATED_P_ID = 2L;

    private static final SignInStatus DEFAULT_SIGN_IN = SignInStatus.DIDNOT;
    private static final SignInStatus UPDATED_SIGN_IN = SignInStatus.SIGNIN;

    private static final Instant DEFAULT_SIGN_IN_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SIGN_IN_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SIGN_IN_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_SIGN_IN_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/student-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudentRecordsRepository studentRecordsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentRecordsMockMvc;

    private StudentRecords studentRecords;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRecords createEntity(EntityManager em) {
        StudentRecords studentRecords = new StudentRecords()
            .crId(DEFAULT_CR_ID)
            .pId(DEFAULT_P_ID)
            .signIn(DEFAULT_SIGN_IN)
            .signInTime(DEFAULT_SIGN_IN_TIME)
            .signInImage(DEFAULT_SIGN_IN_IMAGE);
        return studentRecords;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudentRecords createUpdatedEntity(EntityManager em) {
        StudentRecords studentRecords = new StudentRecords()
            .crId(UPDATED_CR_ID)
            .pId(UPDATED_P_ID)
            .signIn(UPDATED_SIGN_IN)
            .signInTime(UPDATED_SIGN_IN_TIME)
            .signInImage(UPDATED_SIGN_IN_IMAGE);
        return studentRecords;
    }

    @BeforeEach
    public void initTest() {
        studentRecords = createEntity(em);
    }

    @Test
    @Transactional
    void createStudentRecords() throws Exception {
        int databaseSizeBeforeCreate = studentRecordsRepository.findAll().size();
        // Create the StudentRecords
        restStudentRecordsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRecords))
            )
            .andExpect(status().isCreated());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeCreate + 1);
        StudentRecords testStudentRecords = studentRecordsList.get(studentRecordsList.size() - 1);
        assertThat(testStudentRecords.getCrId()).isEqualTo(DEFAULT_CR_ID);
        assertThat(testStudentRecords.getpId()).isEqualTo(DEFAULT_P_ID);
        assertThat(testStudentRecords.getSignIn()).isEqualTo(DEFAULT_SIGN_IN);
        assertThat(testStudentRecords.getSignInTime()).isEqualTo(DEFAULT_SIGN_IN_TIME);
        assertThat(testStudentRecords.getSignInImage()).isEqualTo(DEFAULT_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void createStudentRecordsWithExistingId() throws Exception {
        // Create the StudentRecords with an existing ID
        studentRecords.setId(1L);

        int databaseSizeBeforeCreate = studentRecordsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentRecordsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStudentRecords() throws Exception {
        // Initialize the database
        studentRecordsRepository.saveAndFlush(studentRecords);

        // Get all the studentRecordsList
        restStudentRecordsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studentRecords.getId().intValue())))
            .andExpect(jsonPath("$.[*].crId").value(hasItem(DEFAULT_CR_ID.intValue())))
            .andExpect(jsonPath("$.[*].pId").value(hasItem(DEFAULT_P_ID.intValue())))
            .andExpect(jsonPath("$.[*].signIn").value(hasItem(DEFAULT_SIGN_IN.toString())))
            .andExpect(jsonPath("$.[*].signInTime").value(hasItem(DEFAULT_SIGN_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].signInImage").value(hasItem(DEFAULT_SIGN_IN_IMAGE)));
    }

    @Test
    @Transactional
    void getStudentRecords() throws Exception {
        // Initialize the database
        studentRecordsRepository.saveAndFlush(studentRecords);

        // Get the studentRecords
        restStudentRecordsMockMvc
            .perform(get(ENTITY_API_URL_ID, studentRecords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studentRecords.getId().intValue()))
            .andExpect(jsonPath("$.crId").value(DEFAULT_CR_ID.intValue()))
            .andExpect(jsonPath("$.pId").value(DEFAULT_P_ID.intValue()))
            .andExpect(jsonPath("$.signIn").value(DEFAULT_SIGN_IN.toString()))
            .andExpect(jsonPath("$.signInTime").value(DEFAULT_SIGN_IN_TIME.toString()))
            .andExpect(jsonPath("$.signInImage").value(DEFAULT_SIGN_IN_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingStudentRecords() throws Exception {
        // Get the studentRecords
        restStudentRecordsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStudentRecords() throws Exception {
        // Initialize the database
        studentRecordsRepository.saveAndFlush(studentRecords);

        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();

        // Update the studentRecords
        StudentRecords updatedStudentRecords = studentRecordsRepository.findById(studentRecords.getId()).get();
        // Disconnect from session so that the updates on updatedStudentRecords are not directly saved in db
        em.detach(updatedStudentRecords);
        updatedStudentRecords
            .crId(UPDATED_CR_ID)
            .pId(UPDATED_P_ID)
            .signIn(UPDATED_SIGN_IN)
            .signInTime(UPDATED_SIGN_IN_TIME)
            .signInImage(UPDATED_SIGN_IN_IMAGE);

        restStudentRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudentRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudentRecords))
            )
            .andExpect(status().isOk());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
        StudentRecords testStudentRecords = studentRecordsList.get(studentRecordsList.size() - 1);
        assertThat(testStudentRecords.getCrId()).isEqualTo(UPDATED_CR_ID);
        assertThat(testStudentRecords.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testStudentRecords.getSignIn()).isEqualTo(UPDATED_SIGN_IN);
        assertThat(testStudentRecords.getSignInTime()).isEqualTo(UPDATED_SIGN_IN_TIME);
        assertThat(testStudentRecords.getSignInImage()).isEqualTo(UPDATED_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();
        studentRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studentRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();
        studentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();
        studentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRecordsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studentRecords)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudentRecordsWithPatch() throws Exception {
        // Initialize the database
        studentRecordsRepository.saveAndFlush(studentRecords);

        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();

        // Update the studentRecords using partial update
        StudentRecords partialUpdatedStudentRecords = new StudentRecords();
        partialUpdatedStudentRecords.setId(studentRecords.getId());

        partialUpdatedStudentRecords.pId(UPDATED_P_ID);

        restStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentRecords))
            )
            .andExpect(status().isOk());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
        StudentRecords testStudentRecords = studentRecordsList.get(studentRecordsList.size() - 1);
        assertThat(testStudentRecords.getCrId()).isEqualTo(DEFAULT_CR_ID);
        assertThat(testStudentRecords.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testStudentRecords.getSignIn()).isEqualTo(DEFAULT_SIGN_IN);
        assertThat(testStudentRecords.getSignInTime()).isEqualTo(DEFAULT_SIGN_IN_TIME);
        assertThat(testStudentRecords.getSignInImage()).isEqualTo(DEFAULT_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateStudentRecordsWithPatch() throws Exception {
        // Initialize the database
        studentRecordsRepository.saveAndFlush(studentRecords);

        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();

        // Update the studentRecords using partial update
        StudentRecords partialUpdatedStudentRecords = new StudentRecords();
        partialUpdatedStudentRecords.setId(studentRecords.getId());

        partialUpdatedStudentRecords
            .crId(UPDATED_CR_ID)
            .pId(UPDATED_P_ID)
            .signIn(UPDATED_SIGN_IN)
            .signInTime(UPDATED_SIGN_IN_TIME)
            .signInImage(UPDATED_SIGN_IN_IMAGE);

        restStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudentRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudentRecords))
            )
            .andExpect(status().isOk());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
        StudentRecords testStudentRecords = studentRecordsList.get(studentRecordsList.size() - 1);
        assertThat(testStudentRecords.getCrId()).isEqualTo(UPDATED_CR_ID);
        assertThat(testStudentRecords.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testStudentRecords.getSignIn()).isEqualTo(UPDATED_SIGN_IN);
        assertThat(testStudentRecords.getSignInTime()).isEqualTo(UPDATED_SIGN_IN_TIME);
        assertThat(testStudentRecords.getSignInImage()).isEqualTo(UPDATED_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();
        studentRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studentRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();
        studentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = studentRecordsRepository.findAll().size();
        studentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studentRecords))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudentRecords in the database
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudentRecords() throws Exception {
        // Initialize the database
        studentRecordsRepository.saveAndFlush(studentRecords);

        int databaseSizeBeforeDelete = studentRecordsRepository.findAll().size();

        // Delete the studentRecords
        restStudentRecordsMockMvc
            .perform(delete(ENTITY_API_URL_ID, studentRecords.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudentRecords> studentRecordsList = studentRecordsRepository.findAll();
        assertThat(studentRecordsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
