package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.InMedicalAdvice;
import com.ledar.mono.domain.enumeration.State;
import com.ledar.mono.repository.InMedicalAdviceRepository;
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
 * Integration tests for the {@link InMedicalAdviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InMedicalAdviceResourceIT {

    private static final Long DEFAULT_CURE_NUMBER = 1L;
    private static final Long UPDATED_CURE_NUMBER = 2L;

    private static final String DEFAULT_CURE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NORMS = "AAAAAAAAAA";
    private static final String UPDATED_NORMS = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_CHARGE = "AAAAAAAAAA";
    private static final String UPDATED_CHARGE = "BBBBBBBBBB";

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final Integer DEFAULT_USE_NUMBER = 1;
    private static final Integer UPDATED_USE_NUMBER = 2;

    private static final Long DEFAULT_STAFF_ID = 1L;
    private static final Long UPDATED_STAFF_ID = 2L;

    private static final Long DEFAULT_CURE_ID = 1L;
    private static final Long UPDATED_CURE_ID = 2L;

    private static final String DEFAULT_ID_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUM = "BBBBBBBBBB";

    private static final Long DEFAULT_START_DOCTOR = 1L;
    private static final Long UPDATED_START_DOCTOR = 2L;

    private static final String DEFAULT_START_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_START_DEPARTMENT = "BBBBBBBBBB";

    private static final String DEFAULT_NURSE_DEPARTMENT = "AAAAAAAAAA";
    private static final String UPDATED_NURSE_DEPARTMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_STOP_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STOP_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_NURSE_CONFIRMATION = 1L;
    private static final Long UPDATED_NURSE_CONFIRMATION = 2L;

    private static final State DEFAULT_STATE = State.NOTSEE;
    private static final State UPDATED_STATE = State.SEE;

    private static final Boolean DEFAULT_THIS_SYSTEM = false;
    private static final Boolean UPDATED_THIS_SYSTEM = true;

    private static final String ENTITY_API_URL = "/api/in-medical-advices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InMedicalAdviceRepository inMedicalAdviceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInMedicalAdviceMockMvc;

    private InMedicalAdvice inMedicalAdvice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InMedicalAdvice createEntity(EntityManager em) {
        InMedicalAdvice inMedicalAdvice = new InMedicalAdvice()
            .cureNumber(DEFAULT_CURE_NUMBER)
            .cureName(DEFAULT_CURE_NAME)
            .norms(DEFAULT_NORMS)
            .unit(DEFAULT_UNIT)
            .charge(DEFAULT_CHARGE)
            .price(DEFAULT_PRICE)
            .useNumber(DEFAULT_USE_NUMBER)
            .staffId(DEFAULT_STAFF_ID)
            .cureId(DEFAULT_CURE_ID)
            .idNum(DEFAULT_ID_NUM)
            .startDoctor(DEFAULT_START_DOCTOR)
            .startDepartment(DEFAULT_START_DEPARTMENT)
            .nurseDepartment(DEFAULT_NURSE_DEPARTMENT)
            .startTime(DEFAULT_START_TIME)
            .stopTime(DEFAULT_STOP_TIME)
            .nurseConfirmation(DEFAULT_NURSE_CONFIRMATION)
            .state(DEFAULT_STATE)
            .thisSystem(DEFAULT_THIS_SYSTEM);
        return inMedicalAdvice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InMedicalAdvice createUpdatedEntity(EntityManager em) {
        InMedicalAdvice inMedicalAdvice = new InMedicalAdvice()
            .cureNumber(UPDATED_CURE_NUMBER)
            .cureName(UPDATED_CURE_NAME)
            .norms(UPDATED_NORMS)
            .unit(UPDATED_UNIT)
            .charge(UPDATED_CHARGE)
            .price(UPDATED_PRICE)
            .useNumber(UPDATED_USE_NUMBER)
            .staffId(UPDATED_STAFF_ID)
            .cureId(UPDATED_CURE_ID)
            .idNum(UPDATED_ID_NUM)
            .startDoctor(UPDATED_START_DOCTOR)
            .startDepartment(UPDATED_START_DEPARTMENT)
            .nurseDepartment(UPDATED_NURSE_DEPARTMENT)
            .startTime(UPDATED_START_TIME)
            .stopTime(UPDATED_STOP_TIME)
            .nurseConfirmation(UPDATED_NURSE_CONFIRMATION)
            .state(UPDATED_STATE)
            .thisSystem(UPDATED_THIS_SYSTEM);
        return inMedicalAdvice;
    }

    @BeforeEach
    public void initTest() {
        inMedicalAdvice = createEntity(em);
    }

    @Test
    @Transactional
    void createInMedicalAdvice() throws Exception {
        int databaseSizeBeforeCreate = inMedicalAdviceRepository.findAll().size();
        // Create the InMedicalAdvice
        restInMedicalAdviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isCreated());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeCreate + 1);
        InMedicalAdvice testInMedicalAdvice = inMedicalAdviceList.get(inMedicalAdviceList.size() - 1);
        assertThat(testInMedicalAdvice.getCureNumber()).isEqualTo(DEFAULT_CURE_NUMBER);
        assertThat(testInMedicalAdvice.getCureName()).isEqualTo(DEFAULT_CURE_NAME);
        assertThat(testInMedicalAdvice.getNorms()).isEqualTo(DEFAULT_NORMS);
        assertThat(testInMedicalAdvice.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testInMedicalAdvice.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testInMedicalAdvice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testInMedicalAdvice.getUseNumber()).isEqualTo(DEFAULT_USE_NUMBER);
        assertThat(testInMedicalAdvice.getStaffId()).isEqualTo(DEFAULT_STAFF_ID);
        assertThat(testInMedicalAdvice.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testInMedicalAdvice.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testInMedicalAdvice.getStartDoctor()).isEqualTo(DEFAULT_START_DOCTOR);
        assertThat(testInMedicalAdvice.getStartDepartment()).isEqualTo(DEFAULT_START_DEPARTMENT);
        assertThat(testInMedicalAdvice.getNurseDepartment()).isEqualTo(DEFAULT_NURSE_DEPARTMENT);
        assertThat(testInMedicalAdvice.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testInMedicalAdvice.getStopTime()).isEqualTo(DEFAULT_STOP_TIME);
        assertThat(testInMedicalAdvice.getNurseConfirmation()).isEqualTo(DEFAULT_NURSE_CONFIRMATION);
        assertThat(testInMedicalAdvice.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testInMedicalAdvice.getThisSystem()).isEqualTo(DEFAULT_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void createInMedicalAdviceWithExistingId() throws Exception {
        // Create the InMedicalAdvice with an existing ID
        inMedicalAdvice.setId(1L);

        int databaseSizeBeforeCreate = inMedicalAdviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInMedicalAdviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInMedicalAdvices() throws Exception {
        // Initialize the database
        inMedicalAdviceRepository.saveAndFlush(inMedicalAdvice);

        // Get all the inMedicalAdviceList
        restInMedicalAdviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inMedicalAdvice.getId().intValue())))
            .andExpect(jsonPath("$.[*].cureNumber").value(hasItem(DEFAULT_CURE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].cureName").value(hasItem(DEFAULT_CURE_NAME)))
            .andExpect(jsonPath("$.[*].norms").value(hasItem(DEFAULT_NORMS)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].charge").value(hasItem(DEFAULT_CHARGE)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].useNumber").value(hasItem(DEFAULT_USE_NUMBER)))
            .andExpect(jsonPath("$.[*].staffId").value(hasItem(DEFAULT_STAFF_ID.intValue())))
            .andExpect(jsonPath("$.[*].cureId").value(hasItem(DEFAULT_CURE_ID.intValue())))
            .andExpect(jsonPath("$.[*].idNum").value(hasItem(DEFAULT_ID_NUM)))
            .andExpect(jsonPath("$.[*].startDoctor").value(hasItem(DEFAULT_START_DOCTOR.intValue())))
            .andExpect(jsonPath("$.[*].startDepartment").value(hasItem(DEFAULT_START_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].nurseDepartment").value(hasItem(DEFAULT_NURSE_DEPARTMENT)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].stopTime").value(hasItem(DEFAULT_STOP_TIME.toString())))
            .andExpect(jsonPath("$.[*].nurseConfirmation").value(hasItem(DEFAULT_NURSE_CONFIRMATION.intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].thisSystem").value(hasItem(DEFAULT_THIS_SYSTEM.booleanValue())));
    }

    @Test
    @Transactional
    void getInMedicalAdvice() throws Exception {
        // Initialize the database
        inMedicalAdviceRepository.saveAndFlush(inMedicalAdvice);

        // Get the inMedicalAdvice
        restInMedicalAdviceMockMvc
            .perform(get(ENTITY_API_URL_ID, inMedicalAdvice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inMedicalAdvice.getId().intValue()))
            .andExpect(jsonPath("$.cureNumber").value(DEFAULT_CURE_NUMBER.intValue()))
            .andExpect(jsonPath("$.cureName").value(DEFAULT_CURE_NAME))
            .andExpect(jsonPath("$.norms").value(DEFAULT_NORMS))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.charge").value(DEFAULT_CHARGE))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.useNumber").value(DEFAULT_USE_NUMBER))
            .andExpect(jsonPath("$.staffId").value(DEFAULT_STAFF_ID.intValue()))
            .andExpect(jsonPath("$.cureId").value(DEFAULT_CURE_ID.intValue()))
            .andExpect(jsonPath("$.idNum").value(DEFAULT_ID_NUM))
            .andExpect(jsonPath("$.startDoctor").value(DEFAULT_START_DOCTOR.intValue()))
            .andExpect(jsonPath("$.startDepartment").value(DEFAULT_START_DEPARTMENT))
            .andExpect(jsonPath("$.nurseDepartment").value(DEFAULT_NURSE_DEPARTMENT))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.stopTime").value(DEFAULT_STOP_TIME.toString()))
            .andExpect(jsonPath("$.nurseConfirmation").value(DEFAULT_NURSE_CONFIRMATION.intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.thisSystem").value(DEFAULT_THIS_SYSTEM.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingInMedicalAdvice() throws Exception {
        // Get the inMedicalAdvice
        restInMedicalAdviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInMedicalAdvice() throws Exception {
        // Initialize the database
        inMedicalAdviceRepository.saveAndFlush(inMedicalAdvice);

        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();

        // Update the inMedicalAdvice
        InMedicalAdvice updatedInMedicalAdvice = inMedicalAdviceRepository.findById(inMedicalAdvice.getId()).get();
        // Disconnect from session so that the updates on updatedInMedicalAdvice are not directly saved in db
        em.detach(updatedInMedicalAdvice);
        updatedInMedicalAdvice
            .cureNumber(UPDATED_CURE_NUMBER)
            .cureName(UPDATED_CURE_NAME)
            .norms(UPDATED_NORMS)
            .unit(UPDATED_UNIT)
            .charge(UPDATED_CHARGE)
            .price(UPDATED_PRICE)
            .useNumber(UPDATED_USE_NUMBER)
            .staffId(UPDATED_STAFF_ID)
            .cureId(UPDATED_CURE_ID)
            .idNum(UPDATED_ID_NUM)
            .startDoctor(UPDATED_START_DOCTOR)
            .startDepartment(UPDATED_START_DEPARTMENT)
            .nurseDepartment(UPDATED_NURSE_DEPARTMENT)
            .startTime(UPDATED_START_TIME)
            .stopTime(UPDATED_STOP_TIME)
            .nurseConfirmation(UPDATED_NURSE_CONFIRMATION)
            .state(UPDATED_STATE)
            .thisSystem(UPDATED_THIS_SYSTEM);

        restInMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInMedicalAdvice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInMedicalAdvice))
            )
            .andExpect(status().isOk());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
        InMedicalAdvice testInMedicalAdvice = inMedicalAdviceList.get(inMedicalAdviceList.size() - 1);
        assertThat(testInMedicalAdvice.getCureNumber()).isEqualTo(UPDATED_CURE_NUMBER);
        assertThat(testInMedicalAdvice.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testInMedicalAdvice.getNorms()).isEqualTo(UPDATED_NORMS);
        assertThat(testInMedicalAdvice.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testInMedicalAdvice.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testInMedicalAdvice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInMedicalAdvice.getUseNumber()).isEqualTo(UPDATED_USE_NUMBER);
        assertThat(testInMedicalAdvice.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testInMedicalAdvice.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testInMedicalAdvice.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testInMedicalAdvice.getStartDoctor()).isEqualTo(UPDATED_START_DOCTOR);
        assertThat(testInMedicalAdvice.getStartDepartment()).isEqualTo(UPDATED_START_DEPARTMENT);
        assertThat(testInMedicalAdvice.getNurseDepartment()).isEqualTo(UPDATED_NURSE_DEPARTMENT);
        assertThat(testInMedicalAdvice.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testInMedicalAdvice.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testInMedicalAdvice.getNurseConfirmation()).isEqualTo(UPDATED_NURSE_CONFIRMATION);
        assertThat(testInMedicalAdvice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testInMedicalAdvice.getThisSystem()).isEqualTo(UPDATED_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void putNonExistingInMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();
        inMedicalAdvice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inMedicalAdvice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();
        inMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();
        inMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInMedicalAdviceWithPatch() throws Exception {
        // Initialize the database
        inMedicalAdviceRepository.saveAndFlush(inMedicalAdvice);

        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();

        // Update the inMedicalAdvice using partial update
        InMedicalAdvice partialUpdatedInMedicalAdvice = new InMedicalAdvice();
        partialUpdatedInMedicalAdvice.setId(inMedicalAdvice.getId());

        partialUpdatedInMedicalAdvice
            .cureName(UPDATED_CURE_NAME)
            .norms(UPDATED_NORMS)
            .unit(UPDATED_UNIT)
            .charge(UPDATED_CHARGE)
            .price(UPDATED_PRICE)
            .useNumber(UPDATED_USE_NUMBER)
            .staffId(UPDATED_STAFF_ID)
            .cureId(UPDATED_CURE_ID)
            .idNum(UPDATED_ID_NUM)
            .startDoctor(UPDATED_START_DOCTOR)
            .startDepartment(UPDATED_START_DEPARTMENT)
            .stopTime(UPDATED_STOP_TIME)
            .nurseConfirmation(UPDATED_NURSE_CONFIRMATION)
            .state(UPDATED_STATE);

        restInMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInMedicalAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInMedicalAdvice))
            )
            .andExpect(status().isOk());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
        InMedicalAdvice testInMedicalAdvice = inMedicalAdviceList.get(inMedicalAdviceList.size() - 1);
        assertThat(testInMedicalAdvice.getCureNumber()).isEqualTo(DEFAULT_CURE_NUMBER);
        assertThat(testInMedicalAdvice.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testInMedicalAdvice.getNorms()).isEqualTo(UPDATED_NORMS);
        assertThat(testInMedicalAdvice.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testInMedicalAdvice.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testInMedicalAdvice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInMedicalAdvice.getUseNumber()).isEqualTo(UPDATED_USE_NUMBER);
        assertThat(testInMedicalAdvice.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testInMedicalAdvice.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testInMedicalAdvice.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testInMedicalAdvice.getStartDoctor()).isEqualTo(UPDATED_START_DOCTOR);
        assertThat(testInMedicalAdvice.getStartDepartment()).isEqualTo(UPDATED_START_DEPARTMENT);
        assertThat(testInMedicalAdvice.getNurseDepartment()).isEqualTo(DEFAULT_NURSE_DEPARTMENT);
        assertThat(testInMedicalAdvice.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testInMedicalAdvice.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testInMedicalAdvice.getNurseConfirmation()).isEqualTo(UPDATED_NURSE_CONFIRMATION);
        assertThat(testInMedicalAdvice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testInMedicalAdvice.getThisSystem()).isEqualTo(DEFAULT_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void fullUpdateInMedicalAdviceWithPatch() throws Exception {
        // Initialize the database
        inMedicalAdviceRepository.saveAndFlush(inMedicalAdvice);

        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();

        // Update the inMedicalAdvice using partial update
        InMedicalAdvice partialUpdatedInMedicalAdvice = new InMedicalAdvice();
        partialUpdatedInMedicalAdvice.setId(inMedicalAdvice.getId());

        partialUpdatedInMedicalAdvice
            .cureNumber(UPDATED_CURE_NUMBER)
            .cureName(UPDATED_CURE_NAME)
            .norms(UPDATED_NORMS)
            .unit(UPDATED_UNIT)
            .charge(UPDATED_CHARGE)
            .price(UPDATED_PRICE)
            .useNumber(UPDATED_USE_NUMBER)
            .staffId(UPDATED_STAFF_ID)
            .cureId(UPDATED_CURE_ID)
            .idNum(UPDATED_ID_NUM)
            .startDoctor(UPDATED_START_DOCTOR)
            .startDepartment(UPDATED_START_DEPARTMENT)
            .nurseDepartment(UPDATED_NURSE_DEPARTMENT)
            .startTime(UPDATED_START_TIME)
            .stopTime(UPDATED_STOP_TIME)
            .nurseConfirmation(UPDATED_NURSE_CONFIRMATION)
            .state(UPDATED_STATE)
            .thisSystem(UPDATED_THIS_SYSTEM);

        restInMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInMedicalAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInMedicalAdvice))
            )
            .andExpect(status().isOk());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
        InMedicalAdvice testInMedicalAdvice = inMedicalAdviceList.get(inMedicalAdviceList.size() - 1);
        assertThat(testInMedicalAdvice.getCureNumber()).isEqualTo(UPDATED_CURE_NUMBER);
        assertThat(testInMedicalAdvice.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testInMedicalAdvice.getNorms()).isEqualTo(UPDATED_NORMS);
        assertThat(testInMedicalAdvice.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testInMedicalAdvice.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testInMedicalAdvice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testInMedicalAdvice.getUseNumber()).isEqualTo(UPDATED_USE_NUMBER);
        assertThat(testInMedicalAdvice.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testInMedicalAdvice.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testInMedicalAdvice.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testInMedicalAdvice.getStartDoctor()).isEqualTo(UPDATED_START_DOCTOR);
        assertThat(testInMedicalAdvice.getStartDepartment()).isEqualTo(UPDATED_START_DEPARTMENT);
        assertThat(testInMedicalAdvice.getNurseDepartment()).isEqualTo(UPDATED_NURSE_DEPARTMENT);
        assertThat(testInMedicalAdvice.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testInMedicalAdvice.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testInMedicalAdvice.getNurseConfirmation()).isEqualTo(UPDATED_NURSE_CONFIRMATION);
        assertThat(testInMedicalAdvice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testInMedicalAdvice.getThisSystem()).isEqualTo(UPDATED_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void patchNonExistingInMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();
        inMedicalAdvice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inMedicalAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();
        inMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = inMedicalAdviceRepository.findAll().size();
        inMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inMedicalAdvice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InMedicalAdvice in the database
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInMedicalAdvice() throws Exception {
        // Initialize the database
        inMedicalAdviceRepository.saveAndFlush(inMedicalAdvice);

        int databaseSizeBeforeDelete = inMedicalAdviceRepository.findAll().size();

        // Delete the inMedicalAdvice
        restInMedicalAdviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, inMedicalAdvice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InMedicalAdvice> inMedicalAdviceList = inMedicalAdviceRepository.findAll();
        assertThat(inMedicalAdviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
