package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.ListOfCourses;
import com.ledar.mono.repository.ListOfCoursesRepository;
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
 * Integration tests for the {@link ListOfCoursesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ListOfCoursesResourceIT {

    private static final Long DEFAULT_C_ID = 1L;
    private static final Long UPDATED_C_ID = 2L;

    private static final Long DEFAULT_P_ID = 1L;
    private static final Long UPDATED_P_ID = 2L;

    private static final Long DEFAULT_T_ID = 1L;
    private static final Long UPDATED_T_ID = 2L;

    private static final Long DEFAULT_R_ID = 1L;
    private static final Long UPDATED_R_ID = 2L;

    private static final Instant DEFAULT_SCHOOL_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCHOOL_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLASS_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLASS_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_L_TYPE = false;
    private static final Boolean UPDATED_L_TYPE = true;

    private static final String ENTITY_API_URL = "/api/list-of-courses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ListOfCoursesRepository listOfCoursesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restListOfCoursesMockMvc;

    private ListOfCourses listOfCourses;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListOfCourses createEntity(EntityManager em) {
        ListOfCourses listOfCourses = new ListOfCourses()
            .cId(DEFAULT_C_ID)
            .pId(DEFAULT_P_ID)
            .tId(DEFAULT_T_ID)
            .rId(DEFAULT_R_ID)
            .schoolTime(DEFAULT_SCHOOL_TIME)
            .classTime(DEFAULT_CLASS_TIME)
            .lType(DEFAULT_L_TYPE);
        return listOfCourses;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ListOfCourses createUpdatedEntity(EntityManager em) {
        ListOfCourses listOfCourses = new ListOfCourses()
            .cId(UPDATED_C_ID)
            .pId(UPDATED_P_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .lType(UPDATED_L_TYPE);
        return listOfCourses;
    }

    @BeforeEach
    public void initTest() {
        listOfCourses = createEntity(em);
    }

    @Test
    @Transactional
    void createListOfCourses() throws Exception {
        int databaseSizeBeforeCreate = listOfCoursesRepository.findAll().size();
        // Create the ListOfCourses
        restListOfCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listOfCourses)))
            .andExpect(status().isCreated());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeCreate + 1);
        ListOfCourses testListOfCourses = listOfCoursesList.get(listOfCoursesList.size() - 1);
        assertThat(testListOfCourses.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testListOfCourses.getpId()).isEqualTo(DEFAULT_P_ID);
        assertThat(testListOfCourses.gettId()).isEqualTo(DEFAULT_T_ID);
        assertThat(testListOfCourses.getrId()).isEqualTo(DEFAULT_R_ID);
        assertThat(testListOfCourses.getSchoolTime()).isEqualTo(DEFAULT_SCHOOL_TIME);
        assertThat(testListOfCourses.getClassTime()).isEqualTo(DEFAULT_CLASS_TIME);
        assertThat(testListOfCourses.getlType()).isEqualTo(DEFAULT_L_TYPE);
    }

    @Test
    @Transactional
    void createListOfCoursesWithExistingId() throws Exception {
        // Create the ListOfCourses with an existing ID
        listOfCourses.setId(1L);

        int databaseSizeBeforeCreate = listOfCoursesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restListOfCoursesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listOfCourses)))
            .andExpect(status().isBadRequest());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllListOfCourses() throws Exception {
        // Initialize the database
        listOfCoursesRepository.saveAndFlush(listOfCourses);

        // Get all the listOfCoursesList
        restListOfCoursesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(listOfCourses.getId().intValue())))
            .andExpect(jsonPath("$.[*].cId").value(hasItem(DEFAULT_C_ID.intValue())))
            .andExpect(jsonPath("$.[*].pId").value(hasItem(DEFAULT_P_ID.intValue())))
            .andExpect(jsonPath("$.[*].tId").value(hasItem(DEFAULT_T_ID.intValue())))
            .andExpect(jsonPath("$.[*].rId").value(hasItem(DEFAULT_R_ID.intValue())))
            .andExpect(jsonPath("$.[*].schoolTime").value(hasItem(DEFAULT_SCHOOL_TIME.toString())))
            .andExpect(jsonPath("$.[*].classTime").value(hasItem(DEFAULT_CLASS_TIME.toString())))
            .andExpect(jsonPath("$.[*].lType").value(hasItem(DEFAULT_L_TYPE.booleanValue())));
    }

    @Test
    @Transactional
    void getListOfCourses() throws Exception {
        // Initialize the database
        listOfCoursesRepository.saveAndFlush(listOfCourses);

        // Get the listOfCourses
        restListOfCoursesMockMvc
            .perform(get(ENTITY_API_URL_ID, listOfCourses.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(listOfCourses.getId().intValue()))
            .andExpect(jsonPath("$.cId").value(DEFAULT_C_ID.intValue()))
            .andExpect(jsonPath("$.pId").value(DEFAULT_P_ID.intValue()))
            .andExpect(jsonPath("$.tId").value(DEFAULT_T_ID.intValue()))
            .andExpect(jsonPath("$.rId").value(DEFAULT_R_ID.intValue()))
            .andExpect(jsonPath("$.schoolTime").value(DEFAULT_SCHOOL_TIME.toString()))
            .andExpect(jsonPath("$.classTime").value(DEFAULT_CLASS_TIME.toString()))
            .andExpect(jsonPath("$.lType").value(DEFAULT_L_TYPE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingListOfCourses() throws Exception {
        // Get the listOfCourses
        restListOfCoursesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewListOfCourses() throws Exception {
        // Initialize the database
        listOfCoursesRepository.saveAndFlush(listOfCourses);

        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();

        // Update the listOfCourses
        ListOfCourses updatedListOfCourses = listOfCoursesRepository.findById(listOfCourses.getId()).get();
        // Disconnect from session so that the updates on updatedListOfCourses are not directly saved in db
        em.detach(updatedListOfCourses);
        updatedListOfCourses
            .cId(UPDATED_C_ID)
            .pId(UPDATED_P_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .lType(UPDATED_L_TYPE);

        restListOfCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedListOfCourses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedListOfCourses))
            )
            .andExpect(status().isOk());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
        ListOfCourses testListOfCourses = listOfCoursesList.get(listOfCoursesList.size() - 1);
        assertThat(testListOfCourses.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testListOfCourses.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testListOfCourses.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testListOfCourses.getrId()).isEqualTo(UPDATED_R_ID);
        assertThat(testListOfCourses.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testListOfCourses.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testListOfCourses.getlType()).isEqualTo(UPDATED_L_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingListOfCourses() throws Exception {
        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();
        listOfCourses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListOfCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, listOfCourses.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(listOfCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchListOfCourses() throws Exception {
        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();
        listOfCourses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListOfCoursesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(listOfCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamListOfCourses() throws Exception {
        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();
        listOfCourses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListOfCoursesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(listOfCourses)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateListOfCoursesWithPatch() throws Exception {
        // Initialize the database
        listOfCoursesRepository.saveAndFlush(listOfCourses);

        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();

        // Update the listOfCourses using partial update
        ListOfCourses partialUpdatedListOfCourses = new ListOfCourses();
        partialUpdatedListOfCourses.setId(listOfCourses.getId());

        partialUpdatedListOfCourses.tId(UPDATED_T_ID).classTime(UPDATED_CLASS_TIME);

        restListOfCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedListOfCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedListOfCourses))
            )
            .andExpect(status().isOk());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
        ListOfCourses testListOfCourses = listOfCoursesList.get(listOfCoursesList.size() - 1);
        assertThat(testListOfCourses.getcId()).isEqualTo(DEFAULT_C_ID);
        assertThat(testListOfCourses.getpId()).isEqualTo(DEFAULT_P_ID);
        assertThat(testListOfCourses.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testListOfCourses.getrId()).isEqualTo(DEFAULT_R_ID);
        assertThat(testListOfCourses.getSchoolTime()).isEqualTo(DEFAULT_SCHOOL_TIME);
        assertThat(testListOfCourses.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testListOfCourses.getlType()).isEqualTo(DEFAULT_L_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateListOfCoursesWithPatch() throws Exception {
        // Initialize the database
        listOfCoursesRepository.saveAndFlush(listOfCourses);

        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();

        // Update the listOfCourses using partial update
        ListOfCourses partialUpdatedListOfCourses = new ListOfCourses();
        partialUpdatedListOfCourses.setId(listOfCourses.getId());

        partialUpdatedListOfCourses
            .cId(UPDATED_C_ID)
            .pId(UPDATED_P_ID)
            .tId(UPDATED_T_ID)
            .rId(UPDATED_R_ID)
            .schoolTime(UPDATED_SCHOOL_TIME)
            .classTime(UPDATED_CLASS_TIME)
            .lType(UPDATED_L_TYPE);

        restListOfCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedListOfCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedListOfCourses))
            )
            .andExpect(status().isOk());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
        ListOfCourses testListOfCourses = listOfCoursesList.get(listOfCoursesList.size() - 1);
        assertThat(testListOfCourses.getcId()).isEqualTo(UPDATED_C_ID);
        assertThat(testListOfCourses.getpId()).isEqualTo(UPDATED_P_ID);
        assertThat(testListOfCourses.gettId()).isEqualTo(UPDATED_T_ID);
        assertThat(testListOfCourses.getrId()).isEqualTo(UPDATED_R_ID);
        assertThat(testListOfCourses.getSchoolTime()).isEqualTo(UPDATED_SCHOOL_TIME);
        assertThat(testListOfCourses.getClassTime()).isEqualTo(UPDATED_CLASS_TIME);
        assertThat(testListOfCourses.getlType()).isEqualTo(UPDATED_L_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingListOfCourses() throws Exception {
        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();
        listOfCourses.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restListOfCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, listOfCourses.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(listOfCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchListOfCourses() throws Exception {
        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();
        listOfCourses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListOfCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(listOfCourses))
            )
            .andExpect(status().isBadRequest());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamListOfCourses() throws Exception {
        int databaseSizeBeforeUpdate = listOfCoursesRepository.findAll().size();
        listOfCourses.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restListOfCoursesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(listOfCourses))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ListOfCourses in the database
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteListOfCourses() throws Exception {
        // Initialize the database
        listOfCoursesRepository.saveAndFlush(listOfCourses);

        int databaseSizeBeforeDelete = listOfCoursesRepository.findAll().size();

        // Delete the listOfCourses
        restListOfCoursesMockMvc
            .perform(delete(ENTITY_API_URL_ID, listOfCourses.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ListOfCourses> listOfCoursesList = listOfCoursesRepository.findAll();
        assertThat(listOfCoursesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
