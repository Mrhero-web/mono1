package com.ledar.mono.web.rest;

import com.ledar.mono.common.querydsl.OptionalBooleanBuilder;
import com.ledar.mono.common.response.PaginationUtil;
import com.ledar.mono.domain.Patient;
import com.ledar.mono.domain.QPatient;
import com.ledar.mono.domain.QUser;
import com.ledar.mono.domain.User;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.repository.PatientRepository;
import com.ledar.mono.repository.UserRepository;
import com.ledar.mono.security.SecurityUtils;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liquibase.pro.packaged.id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final UserRepository userRepository;
    private final JPAQueryFactory queryFactory;
    private final QPatient qPatient = QPatient.patient;
    private final QUser qUser = QUser.user;


    public PatientResource(PatientRepository patientRepository, UserRepository userRepository, JPAQueryFactory queryFactory) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.queryFactory = queryFactory;
    }

    @PreAuthorize("hasRole('ROLE_NURSE')")
    @Operation(summary ="新增患者信息",description = "作者：田春晓")
    @PostMapping("/patients/create")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) throws URISyntaxException {
        Patient result = patientRepository.save(patient);
        return ResponseEntity.ok(result);
        //新增账号密码，
    }
    @Operation(summary ="患者信息列表", description="作者：田春晓")
    @GetMapping("/patients/patientsList")
    public ResponseEntity<List<Patient>> patientsList(@RequestParam(required = false) String name,
                                                      Pageable pageable) {
        OptionalBooleanBuilder predicate = new OptionalBooleanBuilder()
            .notEmptyAnd(qPatient.name::contains, name);
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
        Optional<Patient> Patient = patientRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(Patient);
    }
    @Operation(summary="修改病人信息",description="作者：田春晓")
    @PutMapping("/patients/update")
    public ResponseEntity<Patient> updatePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Patient patient
    ) throws URISyntaxException {
        log.debug("REST request to update Patient : {}, {}", id, patient);
     /*   if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }*/
        Patient result = patientRepository.save(patient);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, patient.getId().toString()))
            .body(result);
    }
    @Operation(summary = "获取当前登录患者详情", description = "作者：田春晓")
    @GetMapping("/patients/currentPatient")
    public ResponseEntity<Patient> getCurrentPatient() {
       Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        System.out.println(currentUserLogin);
        Optional<Patient> patient = patientRepository.findPatientByLogin(currentUserLogin);
        //System.out.println(patient);
        return  ResponseUtil.wrapOrNotFound(patient);
        //return ResponseEntity.ok().build();
    }

    @DeleteMapping("/patients/deletetUser")
    @Operation(summary = "删除用户", description = "作者：田春晓")

    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        Patient patient =  patientRepository.findById(id).get();
        Long patientUserId = patient.getUserId();

        User patientUser = userRepository.findById(patientUserId).get();
        //queryFactory.selectFrom(qUser).where(qUser.id.eq(patientUserId)).fetchOne();
        patientUser.setUserStatus(Status.DELETE);
        userRepository.save(patientUser);
        //userRepository.deleteById(id);
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
