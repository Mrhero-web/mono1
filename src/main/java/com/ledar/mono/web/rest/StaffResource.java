package com.ledar.mono.web.rest;

import com.ledar.mono.common.querydsl.OptionalBooleanBuilder;
import com.ledar.mono.common.response.PaginationUtil;
import com.ledar.mono.domain.*;
import com.ledar.mono.domain.enumeration.RoleName;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.repository.*;
import com.ledar.mono.security.SecurityUtils;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

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
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final DepartmentRepository departmentRepository;

    private final JPAQueryFactory queryFactory;
    private final QStaff qStaff = QStaff.staff;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;


    public StaffResource(StaffRepository staffRepository, DepartmentRepository departmentRepository, UserRepository userRepository, JPAQueryFactory queryFactory, UserService userService, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.staffRepository = staffRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.queryFactory = queryFactory;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    /*
     * 科室主任、管理员录入员工信息、新增账号，默认密码123456，patient、staff表不存password，只存login
     * */
//        @PreAuthorize("hasRole('ROLE_DEPARTMENTMANAGER') or hasRole('ROLE_ADMINISTRATOR')")
        @Operation(summary ="新增员工信息以及账号",description = "作者：田春晓")
        @PostMapping("/staff/create")
        public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) throws URISyntaxException {
            //先判断传入的login是否已经存在
            Optional<User> userAlreadyExistsByLogin = userRepository.findOneByLoginAndUserStatusIsNot(staff.getLogin(),Status.DELETE);
            //通过身份证号查询员工
            Optional<Staff> staffAlreadyExistsByIdNum = staffRepository.findOneByIdNum(staff.getIdNum());
            Optional<User> staffAlreadyExistsByIdNumAndUserNotDeleted =null;
            if(staffAlreadyExistsByIdNum.isPresent()) {
                staffAlreadyExistsByIdNumAndUserNotDeleted = userRepository.findByIdAndUserStatusIsNot(staffAlreadyExistsByIdNum.get().getUserId(), Status.DELETE);
            }

            if(userAlreadyExistsByLogin.isPresent()) {
                throw new BadRequestAlertException("登录账号已存在", "", "添加失败。");
            }
            if(staffAlreadyExistsByIdNum.isPresent() && staffAlreadyExistsByIdNumAndUserNotDeleted.isPresent()) {
                throw new BadRequestAlertException("身份证号已存在", "", "添加失败。");
            }
            //1、先新增一条User数据
            User newUser = userService.createUser(staff.getLogin());
            //把新增的userId赋给staff表的userId字段
            staff.setUserId(newUser.getId());

            //2、再新增一条staff数据
            Staff result = staffRepository.save(staff);

            UserRole newUserRole = new UserRole();
            newUserRole.setUserId(newUser.getId());
            //通过 roleName 获取相应的 role 对象，初始角色默认为医生，管理员可以修改
            Optional<Role> role = roleRepository.findByRoleNameInEn(RoleName.DOCTOR);
            //把获取到的roleId赋值给roleId
            newUserRole.setRoleId(role.get().getId());
            //3、最后新增一条UserRole数据，
            userRoleRepository.save(newUserRole);
            return ResponseEntity.ok(result);
        }


    @Operation(summary ="员工信息列表", description="作者：田春晓")
    @GetMapping("/staff/staffList")
    public ResponseEntity<List<Staff>> staffList(@RequestParam(required = false) String name,
                                                 @RequestParam(required = false) Long staffId,
                                                 @RequestParam(required = false) Integer groupId,
                                                 @RequestParam(required = false) String departmentName,
                                                 Pageable pageable) {
        System.out.println(departmentName);
        Integer departmentId = departmentRepository.findDepartmentIdBydName(departmentName);
        System.out.println(departmentId);
        OptionalBooleanBuilder predicate = new OptionalBooleanBuilder()
            .notEmptyAnd(qStaff.groupId ::eq, groupId)
            .notEmptyAnd(qStaff.departmentNum::eq, departmentId)
            .notEmptyAnd(qStaff.name::eq, name)
            .notEmptyAnd(qStaff.id::eq, staffId);
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
     * //@param id the id of the staff to save.
     * @param staff the staff to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staff,
     * or with status {@code 400 (Bad Request)} if the staff is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staff couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Operation(summary = "修改员工信息" , description = "作者：田春晓")
    @PutMapping("/staff/update")
    public ResponseEntity<Staff> updateStaff(//@PathVariable(value = "id", required = false) final Long id,
                                             @Valid @RequestBody Staff staff)
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
        if(staff.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        //相应数据是否已经创建，即是否能在表中找到相应id的数据
        if (!staffRepository.existsById(staff.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Staff oldStaff = staffRepository.findById(staff.getId()).get();
        staff.setUserId(oldStaff.getUserId());
        staff.setLogin(oldStaff.getLogin());
        staff.setPassword(oldStaff.getPassword());
        Staff result = staffRepository.save(staff);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, staff.getId().toString()))
            .body(result);
    }

    //@PreAuthorize("hasRole('ROLE_DOCTOR')")
    @Operation(summary = "获取当前登录员工详情", description = "作者：田春晓")
    @GetMapping("/staff/currentStaff")
    public ResponseEntity<Void> getCurrentStaff() {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        System.out.println(currentUserLogin);
        Optional<Staff> staff = staffRepository.findStaffByLogin(currentUserLogin);
        System.out.println(staff);
        //return  ResponseUtil.wrapOrNotFound(staff);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "删除员工用户", description = "作者：田春晓")
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
    /*@PatchMapping(value = "/staff/{id}", consumes = { "application/json", "application/merge-patch+json" })
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
    }*/

    /**
     * {@code GET  /staff} : get all the staff.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staff in body.
     */
/*    @GetMapping("/staff")
    public List<Staff> getAllStaff() {
        log.debug("REST request to get all Staff");
        return staffRepository.findAll();
    }*/

    /**
     * {@code GET  /staff/:id} : get the "id" staff.
     *
     * @param id the id of the staff to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staff, or with status {@code 404 (Not Found)}.
     */
/*    @GetMapping("/staff/{id}")
    public ResponseEntity<Staff> getStaff(@PathVariable Long id) {
        log.debug("REST request to get Staff : {}", id);
        Optional<Staff> staff = staffRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(staff);
    }*/

    /**
     * {@code DELETE  /staff/:id} : delete the "id" staff.
     *
     * @param id the id of the staff to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*@DeleteMapping("/staff/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        log.debug("REST request to delete Staff : {}", id);
        staffRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }*/
}
