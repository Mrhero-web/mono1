package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.OutMedicalAdvice;
import com.ledar.mono.domain.enumeration.State;
import com.ledar.mono.repository.OutMedicalAdviceRepository;
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
 * Integration tests for the {@link OutMedicalAdviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OutMedicalAdviceResourceIT {

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

    private static final String ENTITY_API_URL = "/api/out-medical-advices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OutMedicalAdviceRepository outMedicalAdviceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOutMedicalAdviceMockMvc;

    private OutMedicalAdvice outMedicalAdvice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutMedicalAdvice createEntity(EntityManager em) {
        OutMedicalAdvice outMedicalAdvice = new OutMedicalAdvice()
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
        return outMedicalAdvice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OutMedicalAdvice createUpdatedEntity(EntityManager em) {
        OutMedicalAdvice outMedicalAdvice = new OutMedicalAdvice()
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
        return outMedicalAdvice;
    }

    @BeforeEach
    public void initTest() {
        outMedicalAdvice = createEntity(em);
    }

    @Test
    @Transactional
    void createOutMedicalAdvice() throws Exception {
        int databaseSizeBeforeCreate = outMedicalAdviceRepository.findAll().size();
        // Create the OutMedicalAdvice
        restOutMedicalAdviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isCreated());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeCreate + 1);
        OutMedicalAdvice testOutMedicalAdvice = outMedicalAdviceList.get(outMedicalAdviceList.size() - 1);
        assertThat(testOutMedicalAdvice.getCureNumber()).isEqualTo(DEFAULT_CURE_NUMBER);
        assertThat(testOutMedicalAdvice.getCureName()).isEqualTo(DEFAULT_CURE_NAME);
        assertThat(testOutMedicalAdvice.getNorms()).isEqualTo(DEFAULT_NORMS);
        assertThat(testOutMedicalAdvice.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testOutMedicalAdvice.getCharge()).isEqualTo(DEFAULT_CHARGE);
        assertThat(testOutMedicalAdvice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOutMedicalAdvice.getUseNumber()).isEqualTo(DEFAULT_USE_NUMBER);
        assertThat(testOutMedicalAdvice.getStaffId()).isEqualTo(DEFAULT_STAFF_ID);
        assertThat(testOutMedicalAdvice.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testOutMedicalAdvice.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testOutMedicalAdvice.getStartDoctor()).isEqualTo(DEFAULT_START_DOCTOR);
        assertThat(testOutMedicalAdvice.getStartDepartment()).isEqualTo(DEFAULT_START_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getNurseDepartment()).isEqualTo(DEFAULT_NURSE_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testOutMedicalAdvice.getStopTime()).isEqualTo(DEFAULT_STOP_TIME);
        assertThat(testOutMedicalAdvice.getNurseConfirmation()).isEqualTo(DEFAULT_NURSE_CONFIRMATION);
        assertThat(testOutMedicalAdvice.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testOutMedicalAdvice.getThisSystem()).isEqualTo(DEFAULT_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void createOutMedicalAdviceWithExistingId() throws Exception {
        // Create the OutMedicalAdvice with an existing ID
        outMedicalAdvice.setId(1L);

        int databaseSizeBeforeCreate = outMedicalAdviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOutMedicalAdviceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOutMedicalAdvices() throws Exception {
        // Initialize the database
        outMedicalAdviceRepository.saveAndFlush(outMedicalAdvice);

        // Get all the outMedicalAdviceList
        restOutMedicalAdviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(outMedicalAdvice.getId().intValue())))
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
    void getOutMedicalAdvice() throws Exception {
        // Initialize the database
        outMedicalAdviceRepository.saveAndFlush(outMedicalAdvice);

        // Get the outMedicalAdvice
        restOutMedicalAdviceMockMvc
            .perform(get(ENTITY_API_URL_ID, outMedicalAdvice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(outMedicalAdvice.getId().intValue()))
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
    void getNonExistingOutMedicalAdvice() throws Exception {
        // Get the outMedicalAdvice
        restOutMedicalAdviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOutMedicalAdvice() throws Exception {
        // Initialize the database
        outMedicalAdviceRepository.saveAndFlush(outMedicalAdvice);

        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();

        // Update the outMedicalAdvice
        OutMedicalAdvice updatedOutMedicalAdvice = outMedicalAdviceRepository.findById(outMedicalAdvice.getId()).get();
        // Disconnect from session so that the updates on updatedOutMedicalAdvice are not directly saved in db
        em.detach(updatedOutMedicalAdvice);
        updatedOutMedicalAdvice
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

        restOutMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOutMedicalAdvice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOutMedicalAdvice))
            )
            .andExpect(status().isOk());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
        OutMedicalAdvice testOutMedicalAdvice = outMedicalAdviceList.get(outMedicalAdviceList.size() - 1);
        assertThat(testOutMedicalAdvice.getCureNumber()).isEqualTo(UPDATED_CURE_NUMBER);
        assertThat(testOutMedicalAdvice.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testOutMedicalAdvice.getNorms()).isEqualTo(UPDATED_NORMS);
        assertThat(testOutMedicalAdvice.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testOutMedicalAdvice.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testOutMedicalAdvice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOutMedicalAdvice.getUseNumber()).isEqualTo(UPDATED_USE_NUMBER);
        assertThat(testOutMedicalAdvice.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testOutMedicalAdvice.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testOutMedicalAdvice.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testOutMedicalAdvice.getStartDoctor()).isEqualTo(UPDATED_START_DOCTOR);
        assertThat(testOutMedicalAdvice.getStartDepartment()).isEqualTo(UPDATED_START_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getNurseDepartment()).isEqualTo(UPDATED_NURSE_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testOutMedicalAdvice.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testOutMedicalAdvice.getNurseConfirmation()).isEqualTo(UPDATED_NURSE_CONFIRMATION);
        assertThat(testOutMedicalAdvice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOutMedicalAdvice.getThisSystem()).isEqualTo(UPDATED_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void putNonExistingOutMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();
        outMedicalAdvice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, outMedicalAdvice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOutMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();
        outMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOutMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();
        outMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutMedicalAdviceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOutMedicalAdviceWithPatch() throws Exception {
        // Initialize the database
        outMedicalAdviceRepository.saveAndFlush(outMedicalAdvice);

        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();

        // Update the outMedicalAdvice using partial update
        OutMedicalAdvice partialUpdatedOutMedicalAdvice = new OutMedicalAdvice();
        partialUpdatedOutMedicalAdvice.setId(outMedicalAdvice.getId());

        partialUpdatedOutMedicalAdvice
            .cureName(UPDATED_CURE_NAME)
            .charge(UPDATED_CHARGE)
            .staffId(UPDATED_STAFF_ID)
            .idNum(UPDATED_ID_NUM)
            .nurseConfirmation(UPDATED_NURSE_CONFIRMATION)
            .state(UPDATED_STATE)
            .thisSystem(UPDATED_THIS_SYSTEM);

        restOutMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutMedicalAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutMedicalAdvice))
            )
            .andExpect(status().isOk());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
        OutMedicalAdvice testOutMedicalAdvice = outMedicalAdviceList.get(outMedicalAdviceList.size() - 1);
        assertThat(testOutMedicalAdvice.getCureNumber()).isEqualTo(DEFAULT_CURE_NUMBER);
        assertThat(testOutMedicalAdvice.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testOutMedicalAdvice.getNorms()).isEqualTo(DEFAULT_NORMS);
        assertThat(testOutMedicalAdvice.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testOutMedicalAdvice.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testOutMedicalAdvice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOutMedicalAdvice.getUseNumber()).isEqualTo(DEFAULT_USE_NUMBER);
        assertThat(testOutMedicalAdvice.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testOutMedicalAdvice.getCureId()).isEqualTo(DEFAULT_CURE_ID);
        assertThat(testOutMedicalAdvice.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testOutMedicalAdvice.getStartDoctor()).isEqualTo(DEFAULT_START_DOCTOR);
        assertThat(testOutMedicalAdvice.getStartDepartment()).isEqualTo(DEFAULT_START_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getNurseDepartment()).isEqualTo(DEFAULT_NURSE_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testOutMedicalAdvice.getStopTime()).isEqualTo(DEFAULT_STOP_TIME);
        assertThat(testOutMedicalAdvice.getNurseConfirmation()).isEqualTo(UPDATED_NURSE_CONFIRMATION);
        assertThat(testOutMedicalAdvice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOutMedicalAdvice.getThisSystem()).isEqualTo(UPDATED_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void fullUpdateOutMedicalAdviceWithPatch() throws Exception {
        // Initialize the database
        outMedicalAdviceRepository.saveAndFlush(outMedicalAdvice);

        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();

        // Update the outMedicalAdvice using partial update
        OutMedicalAdvice partialUpdatedOutMedicalAdvice = new OutMedicalAdvice();
        partialUpdatedOutMedicalAdvice.setId(outMedicalAdvice.getId());

        partialUpdatedOutMedicalAdvice
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

        restOutMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOutMedicalAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOutMedicalAdvice))
            )
            .andExpect(status().isOk());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
        OutMedicalAdvice testOutMedicalAdvice = outMedicalAdviceList.get(outMedicalAdviceList.size() - 1);
        assertThat(testOutMedicalAdvice.getCureNumber()).isEqualTo(UPDATED_CURE_NUMBER);
        assertThat(testOutMedicalAdvice.getCureName()).isEqualTo(UPDATED_CURE_NAME);
        assertThat(testOutMedicalAdvice.getNorms()).isEqualTo(UPDATED_NORMS);
        assertThat(testOutMedicalAdvice.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testOutMedicalAdvice.getCharge()).isEqualTo(UPDATED_CHARGE);
        assertThat(testOutMedicalAdvice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOutMedicalAdvice.getUseNumber()).isEqualTo(UPDATED_USE_NUMBER);
        assertThat(testOutMedicalAdvice.getStaffId()).isEqualTo(UPDATED_STAFF_ID);
        assertThat(testOutMedicalAdvice.getCureId()).isEqualTo(UPDATED_CURE_ID);
        assertThat(testOutMedicalAdvice.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testOutMedicalAdvice.getStartDoctor()).isEqualTo(UPDATED_START_DOCTOR);
        assertThat(testOutMedicalAdvice.getStartDepartment()).isEqualTo(UPDATED_START_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getNurseDepartment()).isEqualTo(UPDATED_NURSE_DEPARTMENT);
        assertThat(testOutMedicalAdvice.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testOutMedicalAdvice.getStopTime()).isEqualTo(UPDATED_STOP_TIME);
        assertThat(testOutMedicalAdvice.getNurseConfirmation()).isEqualTo(UPDATED_NURSE_CONFIRMATION);
        assertThat(testOutMedicalAdvice.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testOutMedicalAdvice.getThisSystem()).isEqualTo(UPDATED_THIS_SYSTEM);
    }

    @Test
    @Transactional
    void patchNonExistingOutMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();
        outMedicalAdvice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOutMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, outMedicalAdvice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOutMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();
        outMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isBadRequest());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOutMedicalAdvice() throws Exception {
        int databaseSizeBeforeUpdate = outMedicalAdviceRepository.findAll().size();
        outMedicalAdvice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOutMedicalAdviceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(outMedicalAdvice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OutMedicalAdvice in the database
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOutMedicalAdvice() throws Exception {
        // Initialize the database
        outMedicalAdviceRepository.saveAndFlush(outMedicalAdvice);

        int databaseSizeBeforeDelete = outMedicalAdviceRepository.findAll().size();

        // Delete the outMedicalAdvice
        restOutMedicalAdviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, outMedicalAdvice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OutMedicalAdvice> outMedicalAdviceList = outMedicalAdviceRepository.findAll();
        assertThat(outMedicalAdviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
