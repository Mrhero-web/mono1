package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.PastStudentRecords;
import com.ledar.mono.domain.enumeration.SignInStatus;
import com.ledar.mono.repository.PastStudentRecordsRepository;
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
 * Integration tests for the {@link PastStudentRecordsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PastStudentRecordsResourceIT {

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

    private static final String ENTITY_API_URL = "/api/past-student-records";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PastStudentRecordsRepository pastStudentRecordsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPastStudentRecordsMockMvc;

    private PastStudentRecords pastStudentRecords;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PastStudentRecords createEntity(EntityManager em) {
        PastStudentRecords pastStudentRecords = new PastStudentRecords()
            .crId(DEFAULT_CR_ID)
            .pId(DEFAULT_P_ID)
            .signIn(DEFAULT_SIGN_IN)
            .signInTime(DEFAULT_SIGN_IN_TIME)
            .signInImage(DEFAULT_SIGN_IN_IMAGE);
        return pastStudentRecords;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PastStudentRecords createUpdatedEntity(EntityManager em) {
        PastStudentRecords pastStudentRecords = new PastStudentRecords()
            .crId(UPDATED_CR_ID)
            .pId(UPDATED_P_ID)
            .signIn(UPDATED_SIGN_IN)
            .signInTime(UPDATED_SIGN_IN_TIME)
            .signInImage(UPDATED_SIGN_IN_IMAGE);
        return pastStudentRecords;
    }

    @BeforeEach
    public void initTest() {
        pastStudentRecords = createEntity(em);
    }

    @Test
    @Transactional
    void createPastStudentRecords() throws Exception {
        int databaseSizeBeforeCreate = pastStudentRecordsRepository.findAll().size();
        // Create the PastStudentRecords
        restPastStudentRecordsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isCreated());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeCreate + 1);
        PastStudentRecords testPastStudentRecords = pastStudentRecordsList.get(pastStudentRecordsList.size() - 1);
        assertThat(testPastStudentRecords.getCrId()).isEqualTo(DEFAULT_CR_ID);
        assertThat(testPastStudentRecords.getpId()).isEqualTo(DEFAULT_P_ID);
        assertThat(testPastStudentRecords.getSignIn()).isEqualTo(DEFAULT_SIGN_IN);
        assertThat(testPastStudentRecords.getSignInTime()).isEqualTo(DEFAULT_SIGN_IN_TIME);
        assertThat(testPastStudentRecords.getSignInImage()).isEqualTo(DEFAULT_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void createPastStudentRecordsWithExistingId() throws Exception {
        // Create the PastStudentRecords with an existing ID
        pastStudentRecords.setId(1L);

        int databaseSizeBeforeCreate = pastStudentRecordsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPastStudentRecordsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPastStudentRecords() throws Exception {
        // Initialize the database
        pastStudentRecordsRepository.saveAndFlush(pastStudentRecords);

        // Get all the pastStudentRecordsList
        restPastStudentRecordsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pastStudentRecords.getId().intValue())))
            .andExpect(jsonPath("$.[*].crId").value(hasItem(DEFAULT_CR_ID.intValue())))
            .andExpect(jsonPath("$.[*].pId").value(hasItem(DEFAULT_P_ID.intValue())))
            .andExpect(jsonPath("$.[*].signIn").value(hasItem(DEFAULT_SIGN_IN.toString())))
            .andExpect(jsonPath("$.[*].signInTime").value(hasItem(DEFAULT_SIGN_IN_TIME.toString())))
            .andExpect(jsonPath("$.[*].signInImage").value(hasItem(DEFAULT_SIGN_IN_IMAGE)));
    }

    @Test
    @Transactional
    void getPastStudentRecords() throws Exception {
        // Initialize the database
        pastStudentRecordsRepository.saveAndFlush(pastStudentRecords);

        // Get the pastStudentRecords
        restPastStudentRecordsMockMvc
            .perform(get(ENTITY_API_URL_ID, pastStudentRecords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pastStudentRecords.getId().intValue()))
            .andExpect(jsonPath("$.crId").value(DEFAULT_CR_ID.intValue()))
            .andExpect(jsonPath("$.pId").value(DEFAULT_P_ID.intValue()))
            .andExpect(jsonPath("$.signIn").value(DEFAULT_SIGN_IN.toString()))
            .andExpect(jsonPath("$.signInTime").value(DEFAULT_SIGN_IN_TIME.toString()))
            .andExpect(jsonPath("$.signInImage").value(DEFAULT_SIGN_IN_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingPastStudentRecords() throws Exception {
        // Get the pastStudentRecords
        restPastStudentRecordsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPastStudentRecords() throws Exception {
        // Initialize the database
        pastStudentRecordsRepository.saveAndFlush(pastStudentRecords);

        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();

        // Update the pastStudentRecords
        PastStudentRecords updatedPastStudentRecords = pastStudentRecordsRepository.findById(pastStudentRecords.getId()).get();
        // Disconnect from session so that the updates on updatedPastStudentRecords are not directly saved in db
        em.detach(updatedPastStudentRecords);
        updatedPastStudentRecords
            .crId(UPDATED_CR_ID)
            .pId(UPDATED_P_ID)
            .signIn(UPDATED_SIGN_IN)
            .signInTime(UPDATED_SIGN_IN_TIME)
            .signInImage(UPDATED_SIGN_IN_IMAGE);

        restPastStudentRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPastStudentRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPastStudentRecords))
            )
            .andExpect(status().isOk());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
        PastStudentRecords testPastStudentRecords = pastStudentRecordsList.get(pastStudentRecordsList.size() - 1);
        assertThat(testPastStudentRecords.getCrId()).isEqualTo(UPDATED_CR_ID);
        assertThat(testPastStudentRecords.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testPastStudentRecords.getSignIn()).isEqualTo(UPDATED_SIGN_IN);
        assertThat(testPastStudentRecords.getSignInTime()).isEqualTo(UPDATED_SIGN_IN_TIME);
        assertThat(testPastStudentRecords.getSignInImage()).isEqualTo(UPDATED_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingPastStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();
        pastStudentRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPastStudentRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pastStudentRecords.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPastStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();
        pastStudentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastStudentRecordsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPastStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();
        pastStudentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastStudentRecordsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePastStudentRecordsWithPatch() throws Exception {
        // Initialize the database
        pastStudentRecordsRepository.saveAndFlush(pastStudentRecords);

        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();

        // Update the pastStudentRecords using partial update
        PastStudentRecords partialUpdatedPastStudentRecords = new PastStudentRecords();
        partialUpdatedPastStudentRecords.setId(pastStudentRecords.getId());

        partialUpdatedPastStudentRecords.pId(UPDATED_P_ID).signIn(UPDATED_SIGN_IN).signInImage(UPDATED_SIGN_IN_IMAGE);

        restPastStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPastStudentRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPastStudentRecords))
            )
            .andExpect(status().isOk());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
        PastStudentRecords testPastStudentRecords = pastStudentRecordsList.get(pastStudentRecordsList.size() - 1);
        assertThat(testPastStudentRecords.getCrId()).isEqualTo(DEFAULT_CR_ID);
        assertThat(testPastStudentRecords.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testPastStudentRecords.getSignIn()).isEqualTo(UPDATED_SIGN_IN);
        assertThat(testPastStudentRecords.getSignInTime()).isEqualTo(DEFAULT_SIGN_IN_TIME);
        assertThat(testPastStudentRecords.getSignInImage()).isEqualTo(UPDATED_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdatePastStudentRecordsWithPatch() throws Exception {
        // Initialize the database
        pastStudentRecordsRepository.saveAndFlush(pastStudentRecords);

        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();

        // Update the pastStudentRecords using partial update
        PastStudentRecords partialUpdatedPastStudentRecords = new PastStudentRecords();
        partialUpdatedPastStudentRecords.setId(pastStudentRecords.getId());

        partialUpdatedPastStudentRecords
            .crId(UPDATED_CR_ID)
            .pId(UPDATED_P_ID)
            .signIn(UPDATED_SIGN_IN)
            .signInTime(UPDATED_SIGN_IN_TIME)
            .signInImage(UPDATED_SIGN_IN_IMAGE);

        restPastStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPastStudentRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPastStudentRecords))
            )
            .andExpect(status().isOk());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
        PastStudentRecords testPastStudentRecords = pastStudentRecordsList.get(pastStudentRecordsList.size() - 1);
        assertThat(testPastStudentRecords.getCrId()).isEqualTo(UPDATED_CR_ID);
        assertThat(testPastStudentRecords.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testPastStudentRecords.getSignIn()).isEqualTo(UPDATED_SIGN_IN);
        assertThat(testPastStudentRecords.getSignInTime()).isEqualTo(UPDATED_SIGN_IN_TIME);
        assertThat(testPastStudentRecords.getSignInImage()).isEqualTo(UPDATED_SIGN_IN_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingPastStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();
        pastStudentRecords.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPastStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pastStudentRecords.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPastStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();
        pastStudentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isBadRequest());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPastStudentRecords() throws Exception {
        int databaseSizeBeforeUpdate = pastStudentRecordsRepository.findAll().size();
        pastStudentRecords.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPastStudentRecordsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pastStudentRecords))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PastStudentRecords in the database
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePastStudentRecords() throws Exception {
        // Initialize the database
        pastStudentRecordsRepository.saveAndFlush(pastStudentRecords);

        int databaseSizeBeforeDelete = pastStudentRecordsRepository.findAll().size();

        // Delete the pastStudentRecords
        restPastStudentRecordsMockMvc
            .perform(delete(ENTITY_API_URL_ID, pastStudentRecords.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PastStudentRecords> pastStudentRecordsList = pastStudentRecordsRepository.findAll();
        assertThat(pastStudentRecordsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
