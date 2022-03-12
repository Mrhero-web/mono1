package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.Therapist;
import com.ledar.mono.repository.TherapistRepository;
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
 * Integration tests for the {@link TherapistResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TherapistResourceIT {

    private static final String DEFAULT_CURE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CURE_ID = 1L;
    private static final Long UPDATED_CURE_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/therapists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TherapistRepository therapistRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTherapistMockMvc;

    private Therapist therapist;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Therapist createEntity(EntityManager em) {
        Therapist therapist = new Therapist().cureName(DEFAULT_CURE_NAME).cureId(DEFAULT_CURE_ID).name(DEFAULT_NAME);
        return therapist;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Therapist createUpdatedEntity(EntityManager em) {
        Therapist therapist = new Therapist().cureName(UPDATED_CURE_NAME).cureId(UPDATED_CURE_ID).name(UPDATED_NAME);
        return therapist;
    }

    @BeforeEach
    public void initTest() {
        therapist = createEntity(em);
    }

    @Test
    @Transactional
    void createTherapist() throws Exception {
        int databaseSizeBeforeCreate = therapistRepository.findAll().size();
        // Create the Therapist
        restTherapistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isCreated());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeCreate + 1);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getCureName()).isEqualTo(DEFAULT_CURE_NAME);
        assertThat(testTherapist.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testTherapist.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTherapistWithExistingId() throws Exception {
        // Create the Therapist with an existing ID
        therapist.setId(1L);

        int databaseSizeBeforeCreate = therapistRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTherapistMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isBadRequest());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTherapists() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get all the therapistList
        restTherapistMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(therapist.getId().intValue())))
            .andExpect(jsonPath("$.[*].cureName").value(hasItem(DEFAULT_CURE_NAME)))
            .andExpect(jsonPath("$.[*].cureId").value(hasItem(DEFAULT_CURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTherapist() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        // Get the therapist
        restTherapistMockMvc
            .perform(get(ENTITY_API_URL_ID, therapist.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(therapist.getId().intValue()))
            .andExpect(jsonPath("$.cureName").value(DEFAULT_CURE_NAME))
            .andExpect(jsonPath("$.cureId").value(DEFAULT_CURE_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTherapist() throws Exception {
        // Get the therapist
        restTherapistMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTherapist() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();

        // Update the therapist
        Therapist updatedTherapist = therapistRepository.findById(therapist.getId()).get();
        // Disconnect from session so that the updates on updatedTherapist are not directly saved in db
        em.detach(updatedTherapist);
        updatedTherapist.cureName(UPDATED_CURE_NAME).cureId(UPDATED_CURE_ID).name(UPDATED_NAME);

        restTherapistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTherapist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTherapist))
            )
            .andExpect(status().isOk());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testTherapist.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testTherapist.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();
        therapist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTherapistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, therapist.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(therapist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();
        therapist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTherapistMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(therapist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();
        therapist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTherapistMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(therapist)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTherapistWithPatch() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();

        // Update the therapist using partial update
        Therapist partialUpdatedTherapist = new Therapist();
        partialUpdatedTherapist.setId(therapist.getId());

        partialUpdatedTherapist.cureId(UPDATED_CURE_ID).name(UPDATED_NAME);

        restTherapistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTherapist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTherapist))
            )
            .andExpect(status().isOk());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getCureName()).isEqualTo(DEFAULT_CURE_NAME);
        assertThat(testTherapist.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testTherapist.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTherapistWithPatch() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();

        // Update the therapist using partial update
        Therapist partialUpdatedTherapist = new Therapist();
        partialUpdatedTherapist.setId(therapist.getId());

        partialUpdatedTherapist.cureName(UPDATED_CURE_NAME).cureId(UPDATED_CURE_ID).name(UPDATED_NAME);

        restTherapistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTherapist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTherapist))
            )
            .andExpect(status().isOk());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
        Therapist testTherapist = therapistList.get(therapistList.size() - 1);
        assertThat(testTherapist.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testTherapist.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testTherapist.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();
        therapist.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTherapistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, therapist.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(therapist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();
        therapist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTherapistMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(therapist))
            )
            .andExpect(status().isBadRequest());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTherapist() throws Exception {
        int databaseSizeBeforeUpdate = therapistRepository.findAll().size();
        therapist.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTherapistMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(therapist))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Therapist in the database
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTherapist() throws Exception {
        // Initialize the database
        therapistRepository.saveAndFlush(therapist);

        int databaseSizeBeforeDelete = therapistRepository.findAll().size();

        // Delete the therapist
        restTherapistMockMvc
            .perform(delete(ENTITY_API_URL_ID, therapist.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Therapist> therapistList = therapistRepository.findAll();
        assertThat(therapistList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
