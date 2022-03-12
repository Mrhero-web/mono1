package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.Patient;
import com.ledar.mono.domain.enumeration.PatientType;
import com.ledar.mono.repository.PatientRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Long DEFAULT_HOSPITAL_ID = 1L;
    private static final Long UPDATED_HOSPITAL_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ID_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ADMISSION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADMISSION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DISCHARGED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DISCHARGED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_ADMISSION_DEPARTMENT_ID = 1L;
    private static final Long UPDATED_ADMISSION_DEPARTMENT_ID = 2L;

    private static final Integer DEFAULT_DAYS_IN_HOSPITAL = 1;
    private static final Integer UPDATED_DAYS_IN_HOSPITAL = 2;

    private static final String DEFAULT_HOSPITAL_PHYSICIAN = "AAAAAAAAAA";
    private static final String UPDATED_HOSPITAL_PHYSICIAN = "BBBBBBBBBB";

    private static final String DEFAULT_THERAPIST = "AAAAAAAAAA";
    private static final String UPDATED_THERAPIST = "BBBBBBBBBB";

    private static final String DEFAULT_ADMISSION_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_ADMISSION_METHOD = "BBBBBBBBBB";

    private static final Long DEFAULT_CURRENT_DEPARTMENT_ID = 1L;
    private static final Long UPDATED_CURRENT_DEPARTMENT_ID = 2L;

    private static final PatientType DEFAULT_PATIENT_TYPE = PatientType.ADULT;
    private static final PatientType UPDATED_PATIENT_TYPE = PatientType.CHILD;

    private static final Boolean DEFAULT_FROM_HOSPITAL_SYSTEM = false;
    private static final Boolean UPDATED_FROM_HOSPITAL_SYSTEM = true;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .userId(DEFAULT_USER_ID)
            .hospitalId(DEFAULT_HOSPITAL_ID)
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .age(DEFAULT_AGE)
            .birthday(DEFAULT_BIRTHDAY)
            .idNum(DEFAULT_ID_NUM)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .admissionDate(DEFAULT_ADMISSION_DATE)
            .dischargedDate(DEFAULT_DISCHARGED_DATE)
            .admissionDepartmentId(DEFAULT_ADMISSION_DEPARTMENT_ID)
            .daysInHospital(DEFAULT_DAYS_IN_HOSPITAL)
            .hospitalPhysician(DEFAULT_HOSPITAL_PHYSICIAN)
            .therapist(DEFAULT_THERAPIST)
            .admissionMethod(DEFAULT_ADMISSION_METHOD)
            .currentDepartmentId(DEFAULT_CURRENT_DEPARTMENT_ID)
            .patientType(DEFAULT_PATIENT_TYPE)
            .fromHospitalSystem(DEFAULT_FROM_HOSPITAL_SYSTEM)
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD);
        return patient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .userId(UPDATED_USER_ID)
            .hospitalId(UPDATED_HOSPITAL_ID)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .age(UPDATED_AGE)
            .birthday(UPDATED_BIRTHDAY)
            .idNum(UPDATED_ID_NUM)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .dischargedDate(UPDATED_DISCHARGED_DATE)
            .admissionDepartmentId(UPDATED_ADMISSION_DEPARTMENT_ID)
            .daysInHospital(UPDATED_DAYS_IN_HOSPITAL)
            .hospitalPhysician(UPDATED_HOSPITAL_PHYSICIAN)
            .therapist(UPDATED_THERAPIST)
            .admissionMethod(UPDATED_ADMISSION_METHOD)
            .currentDepartmentId(UPDATED_CURRENT_DEPARTMENT_ID)
            .patientType(UPDATED_PATIENT_TYPE)
            .fromHospitalSystem(UPDATED_FROM_HOSPITAL_SYSTEM)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();
        // Create the Patient
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testPatient.getHospitalId()).isEqualTo(DEFAULT_HOSPITAL_ID);
        assertThat(testPatient.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPatient.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPatient.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatient.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testPatient.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPatient.getAdmissionDate()).isEqualTo(DEFAULT_ADMISSION_DATE);
        assertThat(testPatient.getDischargedDate()).isEqualTo(DEFAULT_DISCHARGED_DATE);
        assertThat(testPatient.getAdmissionDepartmentId()).isEqualTo(DEFAULT_ADMISSION_DEPARTMENT_ID);
        assertThat(testPatient.getDaysInHospital()).isEqualTo(DEFAULT_DAYS_IN_HOSPITAL);
        assertThat(testPatient.getHospitalPhysician()).isEqualTo(DEFAULT_HOSPITAL_PHYSICIAN);
        assertThat(testPatient.getTherapist()).isEqualTo(DEFAULT_THERAPIST);
        assertThat(testPatient.getAdmissionMethod()).isEqualTo(DEFAULT_ADMISSION_METHOD);
        assertThat(testPatient.getCurrentDepartmentId()).isEqualTo(DEFAULT_CURRENT_DEPARTMENT_ID);
        assertThat(testPatient.getPatientType()).isEqualTo(DEFAULT_PATIENT_TYPE);
        assertThat(testPatient.getFromHospitalSystem()).isEqualTo(DEFAULT_FROM_HOSPITAL_SYSTEM);
        assertThat(testPatient.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testPatient.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createPatientWithExistingId() throws Exception {
        // Create the Patient with an existing ID
        patient.setId(1L);

        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setUserId(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setName(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setIdNum(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrentDepartmentIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setCurrentDepartmentId(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFromHospitalSystemIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setFromHospitalSystem(null);

        // Create the Patient, which fails.

        restPatientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].hospitalId").value(hasItem(DEFAULT_HOSPITAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].idNum").value(hasItem(DEFAULT_ID_NUM)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].admissionDate").value(hasItem(DEFAULT_ADMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dischargedDate").value(hasItem(DEFAULT_DISCHARGED_DATE.toString())))
            .andExpect(jsonPath("$.[*].admissionDepartmentId").value(hasItem(DEFAULT_ADMISSION_DEPARTMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].daysInHospital").value(hasItem(DEFAULT_DAYS_IN_HOSPITAL)))
            .andExpect(jsonPath("$.[*].hospitalPhysician").value(hasItem(DEFAULT_HOSPITAL_PHYSICIAN)))
            .andExpect(jsonPath("$.[*].therapist").value(hasItem(DEFAULT_THERAPIST)))
            .andExpect(jsonPath("$.[*].admissionMethod").value(hasItem(DEFAULT_ADMISSION_METHOD)))
            .andExpect(jsonPath("$.[*].currentDepartmentId").value(hasItem(DEFAULT_CURRENT_DEPARTMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].patientType").value(hasItem(DEFAULT_PATIENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fromHospitalSystem").value(hasItem(DEFAULT_FROM_HOSPITAL_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc
            .perform(get(ENTITY_API_URL_ID, patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.hospitalId").value(DEFAULT_HOSPITAL_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.idNum").value(DEFAULT_ID_NUM))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.admissionDate").value(DEFAULT_ADMISSION_DATE.toString()))
            .andExpect(jsonPath("$.dischargedDate").value(DEFAULT_DISCHARGED_DATE.toString()))
            .andExpect(jsonPath("$.admissionDepartmentId").value(DEFAULT_ADMISSION_DEPARTMENT_ID.intValue()))
            .andExpect(jsonPath("$.daysInHospital").value(DEFAULT_DAYS_IN_HOSPITAL))
            .andExpect(jsonPath("$.hospitalPhysician").value(DEFAULT_HOSPITAL_PHYSICIAN))
            .andExpect(jsonPath("$.therapist").value(DEFAULT_THERAPIST))
            .andExpect(jsonPath("$.admissionMethod").value(DEFAULT_ADMISSION_METHOD))
            .andExpect(jsonPath("$.currentDepartmentId").value(DEFAULT_CURRENT_DEPARTMENT_ID.intValue()))
            .andExpect(jsonPath("$.patientType").value(DEFAULT_PATIENT_TYPE.toString()))
            .andExpect(jsonPath("$.fromHospitalSystem").value(DEFAULT_FROM_HOSPITAL_SYSTEM.booleanValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .userId(UPDATED_USER_ID)
            .hospitalId(UPDATED_HOSPITAL_ID)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .age(UPDATED_AGE)
            .birthday(UPDATED_BIRTHDAY)
            .idNum(UPDATED_ID_NUM)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .dischargedDate(UPDATED_DISCHARGED_DATE)
            .admissionDepartmentId(UPDATED_ADMISSION_DEPARTMENT_ID)
            .daysInHospital(UPDATED_DAYS_IN_HOSPITAL)
            .hospitalPhysician(UPDATED_HOSPITAL_PHYSICIAN)
            .therapist(UPDATED_THERAPIST)
            .admissionMethod(UPDATED_ADMISSION_METHOD)
            .currentDepartmentId(UPDATED_CURRENT_DEPARTMENT_ID)
            .patientType(UPDATED_PATIENT_TYPE)
            .fromHospitalSystem(UPDATED_FROM_HOSPITAL_SYSTEM)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);

        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPatient.getHospitalId()).isEqualTo(UPDATED_HOSPITAL_ID);
        assertThat(testPatient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatient.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPatient.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPatient.getAdmissionDate()).isEqualTo(UPDATED_ADMISSION_DATE);
        assertThat(testPatient.getDischargedDate()).isEqualTo(UPDATED_DISCHARGED_DATE);
        assertThat(testPatient.getAdmissionDepartmentId()).isEqualTo(UPDATED_ADMISSION_DEPARTMENT_ID);
        assertThat(testPatient.getDaysInHospital()).isEqualTo(UPDATED_DAYS_IN_HOSPITAL);
        assertThat(testPatient.getHospitalPhysician()).isEqualTo(UPDATED_HOSPITAL_PHYSICIAN);
        assertThat(testPatient.getTherapist()).isEqualTo(UPDATED_THERAPIST);
        assertThat(testPatient.getAdmissionMethod()).isEqualTo(UPDATED_ADMISSION_METHOD);
        assertThat(testPatient.getCurrentDepartmentId()).isEqualTo(UPDATED_CURRENT_DEPARTMENT_ID);
        assertThat(testPatient.getPatientType()).isEqualTo(UPDATED_PATIENT_TYPE);
        assertThat(testPatient.getFromHospitalSystem()).isEqualTo(UPDATED_FROM_HOSPITAL_SYSTEM);
        assertThat(testPatient.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testPatient.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .userId(UPDATED_USER_ID)
            .hospitalId(UPDATED_HOSPITAL_ID)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .birthday(UPDATED_BIRTHDAY)
            .admissionDepartmentId(UPDATED_ADMISSION_DEPARTMENT_ID)
            .admissionMethod(UPDATED_ADMISSION_METHOD)
            .currentDepartmentId(UPDATED_CURRENT_DEPARTMENT_ID)
            .password(UPDATED_PASSWORD);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPatient.getHospitalId()).isEqualTo(UPDATED_HOSPITAL_ID);
        assertThat(testPatient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testPatient.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPatient.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testPatient.getAdmissionDate()).isEqualTo(DEFAULT_ADMISSION_DATE);
        assertThat(testPatient.getDischargedDate()).isEqualTo(DEFAULT_DISCHARGED_DATE);
        assertThat(testPatient.getAdmissionDepartmentId()).isEqualTo(UPDATED_ADMISSION_DEPARTMENT_ID);
        assertThat(testPatient.getDaysInHospital()).isEqualTo(DEFAULT_DAYS_IN_HOSPITAL);
        assertThat(testPatient.getHospitalPhysician()).isEqualTo(DEFAULT_HOSPITAL_PHYSICIAN);
        assertThat(testPatient.getTherapist()).isEqualTo(DEFAULT_THERAPIST);
        assertThat(testPatient.getAdmissionMethod()).isEqualTo(UPDATED_ADMISSION_METHOD);
        assertThat(testPatient.getCurrentDepartmentId()).isEqualTo(UPDATED_CURRENT_DEPARTMENT_ID);
        assertThat(testPatient.getPatientType()).isEqualTo(DEFAULT_PATIENT_TYPE);
        assertThat(testPatient.getFromHospitalSystem()).isEqualTo(DEFAULT_FROM_HOSPITAL_SYSTEM);
        assertThat(testPatient.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testPatient.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdatePatientWithPatch() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient using partial update
        Patient partialUpdatedPatient = new Patient();
        partialUpdatedPatient.setId(patient.getId());

        partialUpdatedPatient
            .userId(UPDATED_USER_ID)
            .hospitalId(UPDATED_HOSPITAL_ID)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .age(UPDATED_AGE)
            .birthday(UPDATED_BIRTHDAY)
            .idNum(UPDATED_ID_NUM)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .admissionDate(UPDATED_ADMISSION_DATE)
            .dischargedDate(UPDATED_DISCHARGED_DATE)
            .admissionDepartmentId(UPDATED_ADMISSION_DEPARTMENT_ID)
            .daysInHospital(UPDATED_DAYS_IN_HOSPITAL)
            .hospitalPhysician(UPDATED_HOSPITAL_PHYSICIAN)
            .therapist(UPDATED_THERAPIST)
            .admissionMethod(UPDATED_ADMISSION_METHOD)
            .currentDepartmentId(UPDATED_CURRENT_DEPARTMENT_ID)
            .patientType(UPDATED_PATIENT_TYPE)
            .fromHospitalSystem(UPDATED_FROM_HOSPITAL_SYSTEM)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);

        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatient))
            )
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testPatient.getHospitalId()).isEqualTo(UPDATED_HOSPITAL_ID);
        assertThat(testPatient.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPatient.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPatient.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testPatient.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPatient.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testPatient.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testPatient.getAdmissionDate()).isEqualTo(UPDATED_ADMISSION_DATE);
        assertThat(testPatient.getDischargedDate()).isEqualTo(UPDATED_DISCHARGED_DATE);
        assertThat(testPatient.getAdmissionDepartmentId()).isEqualTo(UPDATED_ADMISSION_DEPARTMENT_ID);
        assertThat(testPatient.getDaysInHospital()).isEqualTo(UPDATED_DAYS_IN_HOSPITAL);
        assertThat(testPatient.getHospitalPhysician()).isEqualTo(UPDATED_HOSPITAL_PHYSICIAN);
        assertThat(testPatient.getTherapist()).isEqualTo(UPDATED_THERAPIST);
        assertThat(testPatient.getAdmissionMethod()).isEqualTo(UPDATED_ADMISSION_METHOD);
        assertThat(testPatient.getCurrentDepartmentId()).isEqualTo(UPDATED_CURRENT_DEPARTMENT_ID);
        assertThat(testPatient.getPatientType()).isEqualTo(UPDATED_PATIENT_TYPE);
        assertThat(testPatient.getFromHospitalSystem()).isEqualTo(UPDATED_FROM_HOSPITAL_SYSTEM);
        assertThat(testPatient.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testPatient.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patient))
            )
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();
        patient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, patient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
