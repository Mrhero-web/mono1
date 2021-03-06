package com.ledar.mono.web.rest;

import com.ledar.mono.common.querydsl.OptionalBooleanBuilder;
import com.ledar.mono.common.response.PaginationUtil;
import com.ledar.mono.domain.*;
import com.ledar.mono.domain.enumeration.RoleName;
import com.ledar.mono.repository.*;
import com.ledar.mono.security.SecurityUtils;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import static org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry.hasRole;

/**
 * REST controller for managing {@link com.ledar.mono.domain.Patient}.
 */
@RestController
@RequestMapping("/api")
@Transactional
@Tag(name ="病人相关接口")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "patient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final PatientRepository patientRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JPAQueryFactory queryFactory;

    private final QPatient qPatient = QPatient.patient;
    private final QUser qUser = QUser.user;
    private final UserService userService;


    public PatientResource(PatientRepository patientRepository, DepartmentRepository departmentRepository, UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, JPAQueryFactory queryFactory, UserService userService) {
        this.patientRepository = patientRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.queryFactory = queryFactory;
        this.userService = userService;
    }

/*
* 医生或者护士录入患者信息，默认密码123456，patient、staff表不存password，只存login
* */
    //@PreAuthorize("hasRole('ROLE_NURSE') or hasRole('ROLE_DOCTOR')")
    @Operation(summary ="新增患者信息以及账号",description = "作者：田春晓")
    @PostMapping("/patients/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) throws URISyntaxException {
        //先判断传入的login是否已经存在
        Optional<User> userAlreadyExists = userRepository.findOneByLogin(patient.getLogin());
        if(userAlreadyExists.isPresent()) {
            throw new BadRequestAlertException("登录账号已存在", "", "添加失败。");
        }
        //1、先新增一条User数据
        User newUser = userService.createUser(patient.getLogin());
        //把新增的userId赋给patient表的userId字段
        patient.setUserId(newUser.getId());
        //2、再新增一条patient数据
        Patient result = patientRepository.save(patient);
        UserRole newUserRole = new UserRole();
        newUserRole.setUserId(newUser.getId());
        //通过 roleName 获取相应的 role 对象
        Optional<Role> role = roleRepository.findByRoleNameInEn(RoleName.PATIENT);
        //把获取到的roleId赋值给roleId
        newUserRole.setRoleId(role.get().getId());
        //3、最后新增一条UserRole数据，
        userRoleRepository.save(newUserRole);
        return ResponseEntity.ok(result);
    }

    @Operation(summary="修改病人信息",description="作者：田春晓")
    @PutMapping("/patients/update")
    public ResponseEntity<Patient> updatePatient(
//        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to update Patient : {}, {}",  patient);
     /*   if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }*/
        //id不能为空
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        //相应id的数据是否应经创建，即是否能在表中找到相应id的数据
        if (!patientRepository.existsById(patient.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        //确保该治疗项目以外的其他治疗项目的名字跟传入名字不一致
//        if (
//            treatmentProgramRepository
//                .findAllByIdNot(treatmentProgram.getId())
//                .stream()
//                .map(TreatmentProgram::getCureName)
//                .collect(Collectors.toList())
//                .contains(treatmentProgram.getCureName())
//        ) {
//            throw new IllegalArgumentException("已有同样名称的治疗项目，请重新输入！");
//        }
        Patient oldPatient = patientRepository.findById(patient.getId()).get();
        patient.setUserId(oldPatient.getUserId());
        patient.setLogin(oldPatient.getLogin());
        patient.setPassword(oldPatient.getPassword());

//        oldPatient.setName(patient.getName());
//        oldPatient.setAge(patient.getAge());
//        oldPatient.setGender(patient.getGender());
//        oldPatient.setBirthday(patient.getBirthday());
//        oldPatient.setIdNum(patient.getIdNum());
//        oldPatient.setPhoneNumber(patient.getPhoneNumber());
//        oldPatient.setAdmissionDate(patient.getAdmissionDate());


        Patient result = patientRepository.save(patient);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patient.getId().toString()))
            .body(result);
    }

    @Operation(summary ="患者信息列表", description="作者：田春晓")
    @GetMapping("/patients/patientsList")
    public ResponseEntity<List<Patient>> patientsList(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String departmentName,
                                                      @RequestParam(required = false) Long hospitalId,
                                                      Pageable pageable) throws NullPointerException{
        System.out.println(departmentName);
        Integer departmentId = (departmentRepository.findDepartmentIdBydName(departmentName));
        System.out.println(departmentId);
        Long departmentId2 = null;
        if(departmentId == null){
            departmentId2 = null;
            System.out.println(departmentId2);
        }else{
            departmentId2 = Long.parseLong(String.valueOf(departmentId));
            System.out.println(departmentId2);
        }
        OptionalBooleanBuilder predicate = new OptionalBooleanBuilder()
            .notEmptyAnd(qPatient.name::contains, name)
            .notEmptyAnd(qPatient.currentDepartmentId::eq, departmentId2)
            .notEmptyAnd(qPatient.hospitalId::eq, hospitalId);
        JPAQuery<Patient> jpaQuery = queryFactory.selectFrom(qPatient)
            .where(predicate.build())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset());
        Page<Patient> page = new PageImpl<Patient>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
    @Operation(summary = "患者详情", description = "作者：田春晓")
    @GetMapping("/patients/patientById")
    public ResponseEntity<Patient> getPatientById(@RequestParam Long id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(patient);
    }

    //@PreAuthorize("hasRole('ROLE_PATIENT')")
    @Operation(summary = "获取当前登录患者详情", description = "作者：田春晓")
    @GetMapping("/patients/currentPatient")
    public ResponseEntity<Patient> getCurrentPatient() {
       Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        System.out.println(currentUserLogin);
        Optional<Patient> patient = patientRepository.findPatientByLogin(currentUserLogin);
        System.out.println(patient);
        return  ResponseUtil.wrapOrNotFound(patient);
       // return ResponseEntity.ok().build();
    }

    @PutMapping("/patients/deletetPatientUser")
    @Operation(summary = "删除患者用户", description = "作者：田春晓")

    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        Patient patient =  patientRepository.findById(id).get();
        Long patientUserId = patient.getUserId();
        userService.deleteUser(patientUserId);
        return ResponseEntity.ok().build();
    }
   /* @Operation(summary = "申请取消排程" , description="作者：田春晓")
    @PutMapping("/patients/cancel")
    public ResponseEntity<void> cancel*/

    /**
     * {@code POST  /patients} : Create a new patient.
     *
     * @param patient the patient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patient, or with status {@code 400 (Bad Request)} if the patient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        if (patient.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Patient result = patientRepository.save(patient);
        return ResponseEntity
            .created(new URI("/api/patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }*/


    /**
     * {@code PUT  /patients/:id} : Updates an existing patient.
     *
     * @param id the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
//    @PutMapping("/patients/{id}")
//    public ResponseEntity<Patient> updatePatient(
//        @PathVariable(value = "id", required = false) final Long id,
//        @Valid @RequestBody Patient patient
//    ) throws URISyntaxException {
//        log.debug("REST request to update Patient : {}, {}", id, patient);
//        if (patient.getId() == null) {
//            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
//        }
//        if (!Objects.equals(id, patient.getId())) {
//            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
//        }
//
//        if (!patientRepository.existsById(id)) {
//            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
//        }
//
//        Patient result = patientRepository.save(patient);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patient.getId().toString()))
//            .body(result);
//    }

    /**
     * {@code PATCH  /patients/:id} : Partial updates given fields of an existing patient, field will ignore if it is null
     *
     * @param id the id of the patient to save.
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patient,
     * or with status {@code 400 (Bad Request)} if the patient is not valid,
     * or with status {@code 404 (Not Found)} if the patient is not found,
     * or with status {@code 500 (Internal Server Error)} if the patient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*@PatchMapping(value = "/patients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Patient> partialUpdatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to partial update Patient partially : {}, {}", id, patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Patient> result = patientRepository
            .findById(patient.getId())
            .map(existingPatient -> {
                if (patient.getUserId() != null) {
                    existingPatient.setUserId(patient.getUserId());
                }
                if (patient.getHospitalId() != null) {
                    existingPatient.setHospitalId(patient.getHospitalId());
                }
                if (patient.getName() != null) {
                    existingPatient.setName(patient.getName());
                }
                if (patient.getGender() != null) {
                    existingPatient.setGender(patient.getGender());
                }
                if (patient.getAge() != null) {
                    existingPatient.setAge(patient.getAge());
                }
                if (patient.getBirthday() != null) {
                    existingPatient.setBirthday(patient.getBirthday());
                }
                if (patient.getIdNum() != null) {
                    existingPatient.setIdNum(patient.getIdNum());
                }
                if (patient.getPhoneNumber() != null) {
                    existingPatient.setPhoneNumber(patient.getPhoneNumber());
                }
                if (patient.getAdmissionDate() != null) {
                    existingPatient.setAdmissionDate(patient.getAdmissionDate());
                }
                if (patient.getDischargedDate() != null) {
                    existingPatient.setDischargedDate(patient.getDischargedDate());
                }
                if (patient.getAdmissionDepartmentId() != null) {
                    existingPatient.setAdmissionDepartmentId(patient.getAdmissionDepartmentId());
                }
                if (patient.getDaysInHospital() != null) {
                    existingPatient.setDaysInHospital(patient.getDaysInHospital());
                }
                if (patient.getHospitalPhysician() != null) {
                    existingPatient.setHospitalPhysician(patient.getHospitalPhysician());
                }
                if (patient.getTherapist() != null) {
                    existingPatient.setTherapist(patient.getTherapist());
                }
                if (patient.getAdmissionMethod() != null) {
                    existingPatient.setAdmissionMethod(patient.getAdmissionMethod());
                }
                if (patient.getCurrentDepartmentId() != null) {
                    existingPatient.setCurrentDepartmentId(patient.getCurrentDepartmentId());
                }
                if (patient.getPatientType() != null) {
                    existingPatient.setPatientType(patient.getPatientType());
                }
                if (patient.getFromHospitalSystem() != null) {
                    existingPatient.setFromHospitalSystem(patient.getFromHospitalSystem());
                }
                if (patient.getLogin() != null) {
                    existingPatient.setLogin(patient.getLogin());
                }
                if (patient.getPassword() != null) {
                    existingPatient.setPassword(patient.getPassword());
                }

                return existingPatient;
            })
            .map(patientRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patient.getId().toString())
        );
    }
*/
    /**
     * {@code GET  /patients} : get all the patients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patients in body.
     */
//    @GetMapping("/patients")
//    public List<Patient> getAllPatients() {
//        log.debug("REST request to get all Patients");
//        return patientRepository.findAll();
//    }

    /**
     * {@code GET  /patients/:id} : get the "id" patient.
     *
     * @param id the id of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patient, or with status {@code 404 (Not Found)}.
     */
   /* @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        Optional<Patient> patient = patientRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(patient);
    }*/

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patient.
     *
     * @param id the id of the patient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }*/
}
