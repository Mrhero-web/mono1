package com.ledar.mono.web.rest;

import com.ledar.mono.common.querydsl.OptionalBooleanBuilder;
import com.ledar.mono.common.response.PaginationUtil;
import com.ledar.mono.domain.Patient;
import com.ledar.mono.domain.QStaff;
import com.ledar.mono.domain.Staff;
import com.ledar.mono.domain.User;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.repository.StaffRepository;
import com.ledar.mono.repository.UserRepository;
import com.ledar.mono.security.SecurityUtils;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import liquibase.pro.packaged.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ledar.mono.domain.Staff}.
 */
@Tag(name = "员工相关接口")
@RestController
@RequestMapping("/api")
@Transactional
public class StaffResource {

    private final Logger log = LoggerFactory.getLogger(StaffResource.class);

    private static final String ENTITY_NAME = "staff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaffRepository staffRepository;
    private final JPAQueryFactory queryFactory;
    private final QStaff qStaff = QStaff.staff;
    private final UserRepository userRepository;

    public StaffResource(StaffRepository staffRepository, UserRepository userRepository,JPAQueryFactory queryFactory) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
        this.queryFactory = queryFactory;
    }

    /**
     * {@code POST  /staff} : Create a new staff.
     *
     * @param staff the staff to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staff, or with status {@code 400 (Bad Request)} if the staff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(summary = "新增员工信息",description ="作者：田春晓")
    @PostMapping("/staff/create")
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) throws URISyntaxException {
       /* log.debug("REST request to save Staff : {}", staff);
        if (staff.getId() != null) {
            throw new BadRequestAlertException("A new staff cannot already have an ID", ENTITY_NAME, "idexists");
        }*/
        Staff result = staffRepository.save(staff);
        return ResponseEntity.ok(result);
            //.created(new URI("/api/staff/" + result.getId()))
            //.headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            //.body(result);
    }

    @Operation(summary ="员工信息列表", description="作者：田春晓")
    @GetMapping("/staff/staffList")
    public ResponseEntity<List<Staff>> staffList(@RequestParam(required = false) String name,
                                                      Pageable pageable) {
        OptionalBooleanBuilder predicate = new OptionalBooleanBuilder()
            .notEmptyAnd(qStaff.name::contains, name);
        JPAQuery<Staff> jpaQuery = queryFactory.selectFrom(qStaff)
            .where(predicate.build())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset());
        Page<Staff> page = new PageImpl<Staff>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Operation(summary = "员工详情", description = "作者：田春晓")
    @GetMapping("/staff/staffById")
    public ResponseEntity<Staff> getPatientById(@RequestParam Long id) {
        Optional<Staff> staff = staffRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(staff);
    }
    /**
     * {@code PUT  /staff/:id} : Updates an existing staff.
     *
     * @param id the id of the staff to save.
     * @param staff the staff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staff,
     * or with status {@code 400 (Bad Request)} if the staff is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(summary = "修改员工信息" , description = "作者：田春晓")
    @PutMapping("/staff/update")
    public ResponseEntity<Staff> updateStaff(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Staff staff)
        throws URISyntaxException {
        /*log.debug("REST request to update Staff : {}, {}", id, staff);
        if (staff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staff.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
*/
        Staff result = staffRepository.save(staff);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, staff.getId().toString()))
            .body(result);
    }

    @Operation(summary = "获取当前登录员工详情", description = "作者：田春晓")
    @GetMapping("/staff/currentStaff")
    public ResponseEntity<Staff> getCurrentStaff() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        System.out.println(currentUserLogin);
        Optional<Staff> staff = staffRepository.findStaffByLogin(currentUserLogin);
        //System.out.println(staff);
        return  ResponseUtil.wrapOrNotFound(staff);
        //return ResponseEntity.ok().build();
    }

    @Operation(summary = "删除用户", description = "作者：田春晓")
    @DeleteMapping("/staff/deletetUser")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        //根据StaffId将userStatus置为DELETE
        Staff staff =  staffRepository.findById(id).get();
        Long staffUserId = staff.getUserId();
        User staffUser = userRepository.findById(staffUserId).get();
        //queryFactory.selectFrom(qUser).where(qUser.id.eq(patientUserId)).fetchOne();
        staffUser.setUserStatus(Status.DELETE);
        userRepository.save(staffUser);
        //userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    /**
     * {@code PATCH  /staff/:id} : Partial updates given fields of an existing staff, field will ignore if it is null
     *
     * @param id the id of the staff to save.
     * @param staff the staff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staff,
     * or with status {@code 400 (Bad Request)} if the staff is not valid,
     * or with status {@code 404 (Not Found)} if the staff is not found,
     * or with status {@code 500 (Internal Server Error)} if the staff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/staff/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Staff> partialUpdateStaff(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Staff staff
    ) throws URISyntaxException {
        log.debug("REST request to partial update Staff partially : {}, {}", id, staff);
        if (staff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staff.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Staff> result = staffRepository
            .findById(staff.getId())
            .map(existingStaff -> {
                if (staff.getUserId() != null) {
                    existingStaff.setUserId(staff.getUserId());
                }
                if (staff.getName() != null) {
                    existingStaff.setName(staff.getName());
                }
                if (staff.getGender() != null) {
                    existingStaff.setGender(staff.getGender());
                }
                if (staff.getEducation() != null) {
                    existingStaff.setEducation(staff.getEducation());
                }
                if (staff.getMajor() != null) {
                    existingStaff.setMajor(staff.getMajor());
                }
                if (staff.getTitle() != null) {
                    existingStaff.setTitle(staff.getTitle());
                }
                if (staff.getPhoneNum() != null) {
                    existingStaff.setPhoneNum(staff.getPhoneNum());
                }
                if (staff.getAddress() != null) {
                    existingStaff.setAddress(staff.getAddress());
                }
                if (staff.getBirthday() != null) {
                    existingStaff.setBirthday(staff.getBirthday());
                }
                if (staff.getIdNum() != null) {
                    existingStaff.setIdNum(staff.getIdNum());
                }
                if (staff.getDepartmentNum() != null) {
                    existingStaff.setDepartmentNum(staff.getDepartmentNum());
                }
                if (staff.getsStatus() != null) {
                    existingStaff.setsStatus(staff.getsStatus());
                }
                if (staff.getPoliticalAffiliation() != null) {
                    existingStaff.setPoliticalAffiliation(staff.getPoliticalAffiliation());
                }
                if (staff.getNationality() != null) {
                    existingStaff.setNationality(staff.getNationality());
                }
                if (staff.getGroupId() != null) {
                    existingStaff.setGroupId(staff.getGroupId());
                }
                if (staff.getFromHospitalSystem() != null) {
                    existingStaff.setFromHospitalSystem(staff.getFromHospitalSystem());
                }
                if (staff.getLogin() != null) {
                    existingStaff.setLogin(staff.getLogin());
                }
                if (staff.getPassword() != null) {
                    existingStaff.setPassword(staff.getPassword());
                }

                return existingStaff;
            })
            .map(staffRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, staff.getId().toString())
        );
    }

    /**
     * {@code GET  /staff} : get all the staff.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staff in body.
     */
    @GetMapping("/staff")
    public List<Staff> getAllStaff() {
        log.debug("REST request to get all Staff");
        return staffRepository.findAll();
    }

    /**
     * {@code GET  /staff/:id} : get the "id" staff.
     *
     * @param id the id of the staff to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staff, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/staff/{id}")
    public ResponseEntity<Staff> getStaff(@PathVariable Long id) {
        log.debug("REST request to get Staff : {}", id);
        Optional<Staff> staff = staffRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(staff);
    }

    /**
     * {@code DELETE  /staff/:id} : delete the "id" staff.
     *
     * @param id the id of the staff to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/staff/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        log.debug("REST request to delete Staff : {}", id);
        staffRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
