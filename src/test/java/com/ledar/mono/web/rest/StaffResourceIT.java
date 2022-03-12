package com.ledar.mono.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ledar.mono.IntegrationTest;
import com.ledar.mono.domain.Staff;
import com.ledar.mono.domain.enumeration.SStatus;
import com.ledar.mono.repository.StaffRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link StaffResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StaffResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_EDUCATION = "AAAAAAAAAA";
    private static final String UPDATED_EDUCATION = "BBBBBBBBBB";

    private static final String DEFAULT_MAJOR = "AAAAAAAAAA";
    private static final String UPDATED_MAJOR = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUM = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ID_NUM = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUM = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEPARTMENT_NUM = 1;
    private static final Integer UPDATED_DEPARTMENT_NUM = 2;

    private static final SStatus DEFAULT_S_STATUS = SStatus.BUSY;
    private static final SStatus UPDATED_S_STATUS = SStatus.FREE;

    private static final String DEFAULT_POLITICAL_AFFILIATION = "AAAAAAAAAA";
    private static final String UPDATED_POLITICAL_AFFILIATION = "BBBBBBBBBB";

    private static final String DEFAULT_NATIONALITY = "AAAAAAAAAA";
    private static final String UPDATED_NATIONALITY = "BBBBBBBBBB";

    private static final Integer DEFAULT_GROUP_ID = 1;
    private static final Integer UPDATED_GROUP_ID = 2;

    private static final Boolean DEFAULT_FROM_HOSPITAL_SYSTEM = false;
    private static final Boolean UPDATED_FROM_HOSPITAL_SYSTEM = true;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/staff";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaffMockMvc;

    private Staff staff;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Staff createEntity(EntityManager em) {
        Staff staff = new Staff()
            .userId(DEFAULT_USER_ID)
            .name(DEFAULT_NAME)
            .gender(DEFAULT_GENDER)
            .education(DEFAULT_EDUCATION)
            .major(DEFAULT_MAJOR)
            .title(DEFAULT_TITLE)
            .phoneNum(DEFAULT_PHONE_NUM)
            .address(DEFAULT_ADDRESS)
            .birthday(DEFAULT_BIRTHDAY)
            .idNum(DEFAULT_ID_NUM)
            .departmentNum(DEFAULT_DEPARTMENT_NUM)
            .sStatus(DEFAULT_S_STATUS)
            .politicalAffiliation(DEFAULT_POLITICAL_AFFILIATION)
            .nationality(DEFAULT_NATIONALITY)
            .groupId(DEFAULT_GROUP_ID)
            .fromHospitalSystem(DEFAULT_FROM_HOSPITAL_SYSTEM)
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD);
        return staff;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Staff createUpdatedEntity(EntityManager em) {
        Staff staff = new Staff()
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .education(UPDATED_EDUCATION)
            .major(UPDATED_MAJOR)
            .title(UPDATED_TITLE)
            .phoneNum(UPDATED_PHONE_NUM)
            .address(UPDATED_ADDRESS)
            .birthday(UPDATED_BIRTHDAY)
            .idNum(UPDATED_ID_NUM)
            .departmentNum(UPDATED_DEPARTMENT_NUM)
            .sStatus(UPDATED_S_STATUS)
            .politicalAffiliation(UPDATED_POLITICAL_AFFILIATION)
            .nationality(UPDATED_NATIONALITY)
            .groupId(UPDATED_GROUP_ID)
            .fromHospitalSystem(UPDATED_FROM_HOSPITAL_SYSTEM)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);
        return staff;
    }

    @BeforeEach
    public void initTest() {
        staff = createEntity(em);
    }

    @Test
    @Transactional
    void createStaff() throws Exception {
        int databaseSizeBeforeCreate = staffRepository.findAll().size();
        // Create the Staff
        restStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isCreated());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeCreate + 1);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaff.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testStaff.getEducation()).isEqualTo(DEFAULT_EDUCATION);
        assertThat(testStaff.getMajor()).isEqualTo(DEFAULT_MAJOR);
        assertThat(testStaff.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testStaff.getPhoneNum()).isEqualTo(DEFAULT_PHONE_NUM);
        assertThat(testStaff.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testStaff.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testStaff.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testStaff.getDepartmentNum()).isEqualTo(DEFAULT_DEPARTMENT_NUM);
        assertThat(testStaff.getsStatus()).isEqualTo(DEFAULT_S_STATUS);
        assertThat(testStaff.getPoliticalAffiliation()).isEqualTo(DEFAULT_POLITICAL_AFFILIATION);
        assertThat(testStaff.getNationality()).isEqualTo(DEFAULT_NATIONALITY);
        assertThat(testStaff.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testStaff.getFromHospitalSystem()).isEqualTo(DEFAULT_FROM_HOSPITAL_SYSTEM);
        assertThat(testStaff.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testStaff.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createStaffWithExistingId() throws Exception {
        // Create the Staff with an existing ID
        staff.setId(1L);

        int databaseSizeBeforeCreate = staffRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setUserId(null);

        // Create the Staff, which fails.

        restStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setName(null);

        // Create the Staff, which fails.

        restStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setIdNum(null);

        // Create the Staff, which fails.

        restStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartmentNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setDepartmentNum(null);

        // Create the Staff, which fails.

        restStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFromHospitalSystemIsRequired() throws Exception {
        int databaseSizeBeforeTest = staffRepository.findAll().size();
        // set the field null
        staff.setFromHospitalSystem(null);

        // Create the Staff, which fails.

        restStaffMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isBadRequest());

        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get all the staffList
        restStaffMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staff.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].education").value(hasItem(DEFAULT_EDUCATION)))
            .andExpect(jsonPath("$.[*].major").value(hasItem(DEFAULT_MAJOR)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].phoneNum").value(hasItem(DEFAULT_PHONE_NUM)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].idNum").value(hasItem(DEFAULT_ID_NUM)))
            .andExpect(jsonPath("$.[*].departmentNum").value(hasItem(DEFAULT_DEPARTMENT_NUM)))
            .andExpect(jsonPath("$.[*].sStatus").value(hasItem(DEFAULT_S_STATUS.toString())))
            .andExpect(jsonPath("$.[*].politicalAffiliation").value(hasItem(DEFAULT_POLITICAL_AFFILIATION)))
            .andExpect(jsonPath("$.[*].nationality").value(hasItem(DEFAULT_NATIONALITY)))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID)))
            .andExpect(jsonPath("$.[*].fromHospitalSystem").value(hasItem(DEFAULT_FROM_HOSPITAL_SYSTEM.booleanValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        // Get the staff
        restStaffMockMvc
            .perform(get(ENTITY_API_URL_ID, staff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staff.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.education").value(DEFAULT_EDUCATION))
            .andExpect(jsonPath("$.major").value(DEFAULT_MAJOR))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.phoneNum").value(DEFAULT_PHONE_NUM))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.idNum").value(DEFAULT_ID_NUM))
            .andExpect(jsonPath("$.departmentNum").value(DEFAULT_DEPARTMENT_NUM))
            .andExpect(jsonPath("$.sStatus").value(DEFAULT_S_STATUS.toString()))
            .andExpect(jsonPath("$.politicalAffiliation").value(DEFAULT_POLITICAL_AFFILIATION))
            .andExpect(jsonPath("$.nationality").value(DEFAULT_NATIONALITY))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID))
            .andExpect(jsonPath("$.fromHospitalSystem").value(DEFAULT_FROM_HOSPITAL_SYSTEM.booleanValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingStaff() throws Exception {
        // Get the staff
        restStaffMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff
        Staff updatedStaff = staffRepository.findById(staff.getId()).get();
        // Disconnect from session so that the updates on updatedStaff are not directly saved in db
        em.detach(updatedStaff);
        updatedStaff
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .education(UPDATED_EDUCATION)
            .major(UPDATED_MAJOR)
            .title(UPDATED_TITLE)
            .phoneNum(UPDATED_PHONE_NUM)
            .address(UPDATED_ADDRESS)
            .birthday(UPDATED_BIRTHDAY)
            .idNum(UPDATED_ID_NUM)
            .departmentNum(UPDATED_DEPARTMENT_NUM)
            .sStatus(UPDATED_S_STATUS)
            .politicalAffiliation(UPDATED_POLITICAL_AFFILIATION)
            .nationality(UPDATED_NATIONALITY)
            .groupId(UPDATED_GROUP_ID)
            .fromHospitalSystem(UPDATED_FROM_HOSPITAL_SYSTEM)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);

        restStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStaff.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStaff))
            )
            .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaff.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testStaff.getEducation()).isEqualTo(UPDATED_EDUCATION);
        assertThat(testStaff.getMajor()).isEqualTo(UPDATED_MAJOR);
        assertThat(testStaff.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStaff.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testStaff.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStaff.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testStaff.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testStaff.getDepartmentNum()).isEqualTo(UPDATED_DEPARTMENT_NUM);
        assertThat(testStaff.getsStatus()).isEqualTo(UPDATED_S_STATUS);
        assertThat(testStaff.getPoliticalAffiliation()).isEqualTo(UPDATED_POLITICAL_AFFILIATION);
        assertThat(testStaff.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testStaff.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testStaff.getFromHospitalSystem()).isEqualTo(UPDATED_FROM_HOSPITAL_SYSTEM);
        assertThat(testStaff.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testStaff.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staff.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaffWithPatch() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff using partial update
        Staff partialUpdatedStaff = new Staff();
        partialUpdatedStaff.setId(staff.getId());

        partialUpdatedStaff
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .major(UPDATED_MAJOR)
            .phoneNum(UPDATED_PHONE_NUM)
            .address(UPDATED_ADDRESS)
            .birthday(UPDATED_BIRTHDAY)
            .sStatus(UPDATED_S_STATUS)
            .politicalAffiliation(UPDATED_POLITICAL_AFFILIATION)
            .nationality(UPDATED_NATIONALITY)
            .password(UPDATED_PASSWORD);

        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaff))
            )
            .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaff.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testStaff.getEducation()).isEqualTo(DEFAULT_EDUCATION);
        assertThat(testStaff.getMajor()).isEqualTo(UPDATED_MAJOR);
        assertThat(testStaff.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testStaff.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testStaff.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStaff.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testStaff.getIdNum()).isEqualTo(DEFAULT_ID_NUM);
        assertThat(testStaff.getDepartmentNum()).isEqualTo(DEFAULT_DEPARTMENT_NUM);
        assertThat(testStaff.getsStatus()).isEqualTo(UPDATED_S_STATUS);
        assertThat(testStaff.getPoliticalAffiliation()).isEqualTo(UPDATED_POLITICAL_AFFILIATION);
        assertThat(testStaff.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testStaff.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
        assertThat(testStaff.getFromHospitalSystem()).isEqualTo(DEFAULT_FROM_HOSPITAL_SYSTEM);
        assertThat(testStaff.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testStaff.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateStaffWithPatch() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeUpdate = staffRepository.findAll().size();

        // Update the staff using partial update
        Staff partialUpdatedStaff = new Staff();
        partialUpdatedStaff.setId(staff.getId());

        partialUpdatedStaff
            .userId(UPDATED_USER_ID)
            .name(UPDATED_NAME)
            .gender(UPDATED_GENDER)
            .education(UPDATED_EDUCATION)
            .major(UPDATED_MAJOR)
            .title(UPDATED_TITLE)
            .phoneNum(UPDATED_PHONE_NUM)
            .address(UPDATED_ADDRESS)
            .birthday(UPDATED_BIRTHDAY)
            .idNum(UPDATED_ID_NUM)
            .departmentNum(UPDATED_DEPARTMENT_NUM)
            .sStatus(UPDATED_S_STATUS)
            .politicalAffiliation(UPDATED_POLITICAL_AFFILIATION)
            .nationality(UPDATED_NATIONALITY)
            .groupId(UPDATED_GROUP_ID)
            .fromHospitalSystem(UPDATED_FROM_HOSPITAL_SYSTEM)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);

        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaff))
            )
            .andExpect(status().isOk());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
        Staff testStaff = staffList.get(staffList.size() - 1);
        assertThat(testStaff.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaff.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testStaff.getEducation()).isEqualTo(UPDATED_EDUCATION);
        assertThat(testStaff.getMajor()).isEqualTo(UPDATED_MAJOR);
        assertThat(testStaff.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testStaff.getPhoneNum()).isEqualTo(UPDATED_PHONE_NUM);
        assertThat(testStaff.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStaff.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testStaff.getIdNum()).isEqualTo(UPDATED_ID_NUM);
        assertThat(testStaff.getDepartmentNum()).isEqualTo(UPDATED_DEPARTMENT_NUM);
        assertThat(testStaff.getsStatus()).isEqualTo(UPDATED_S_STATUS);
        assertThat(testStaff.getPoliticalAffiliation()).isEqualTo(UPDATED_POLITICAL_AFFILIATION);
        assertThat(testStaff.getNationality()).isEqualTo(UPDATED_NATIONALITY);
        assertThat(testStaff.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
        assertThat(testStaff.getFromHospitalSystem()).isEqualTo(UPDATED_FROM_HOSPITAL_SYSTEM);
        assertThat(testStaff.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testStaff.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staff.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staff))
            )
            .andExpect(status().isBadRequest());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaff() throws Exception {
        int databaseSizeBeforeUpdate = staffRepository.findAll().size();
        staff.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaffMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(staff)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Staff in the database
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaff() throws Exception {
        // Initialize the database
        staffRepository.saveAndFlush(staff);

        int databaseSizeBeforeDelete = staffRepository.findAll().size();

        // Delete the staff
        restStaffMockMvc
            .perform(delete(ENTITY_API_URL_ID, staff.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Staff> staffList = staffRepository.findAll();
        assertThat(staffList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
