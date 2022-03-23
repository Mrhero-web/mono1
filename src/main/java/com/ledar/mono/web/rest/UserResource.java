package com.ledar.mono.web.rest;

//import com.ledar.mono.security.CurrentUser;
import com.ledar.mono.common.querydsl.OptionalBooleanBuilder;
import com.ledar.mono.common.response.PaginationUtil;
import com.ledar.mono.domain.*;
import com.ledar.mono.domain.enumeration.RoleName;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.repository.*;
//import com.ruoweiedu.bettex.web.rest.util.PaginationUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.ResponseUtil;
//import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TbDict.
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户相关接口")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PatientRepository patientRepository;
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final QUser qUser = QUser.user;
    private final QPatient qPatient = QPatient.patient;
    private final JPAQueryFactory queryFactory;


    public UserResource(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository, StaffRepository staffRepository, PatientRepository patientRepository, PasswordEncoder passwordEncoder, UserService userService, JPAQueryFactory queryFactory) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.staffRepository = staffRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.queryFactory = queryFactory;
    }
/*凡是能登录的用户都可以修改密码*/
    @Transactional
    @PutMapping("/user/updatePassword")
    @Operation(summary = "修改密码", description = "作者：田春晓")
    public ResponseEntity<Void> updatedPassword(@RequestParam("newPassword") String newPassword,
                                                @RequestParam("oldPassword") String oldPassword) {
      userService.changePassword(oldPassword,newPassword);
      return ResponseEntity.ok().build();
    }

    /**
     * 管理员修改员工的角色
     *
     * @param staffId
     * @param roleIdList
     * @author 田春晓
     *
     */
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/assignRoleToStaff")
    @Operation(summary = "分配角色给员工", description = "作者：田春晓")
    public ResponseEntity<Void> assignRoleToStaff(@RequestParam Long staffId,
                                                  @RequestParam List<Long> roleIdList) {
        //获取staff的userId后，
        //Long userId = staffRepository.findUserIdById(staffId);
        Staff staff = staffRepository.findById(staffId).get();
        Long userId = staff.getUserId();
        //System.out.println(userId);
        //调用userService的修改用户角色方法
        userService.updateUserRole(userId, roleIdList);
        System.out.println("ok");
        return ResponseEntity.ok().build();
    }

    /*@Transactional
    @PostMapping("/user/createUser")
    @Operation(summary = "新增用户", description = "作者：田春晓")
    public ResponseEntity<User> creteUser(@RequestParam String Login) {
        User newUser = userService.createUser(Login);
        return ResponseEntity.ok().body(newUser);
    }*/
/*
* 根据传入参数查询展示相应数据，如果没有相应参数，展示全部数据
* 或许可以在每条数据的后面加上重置或修改密码按钮
* */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary ="用户信息列表", description="作者：田春晓")
    @GetMapping("/user/userssList")
    public ResponseEntity<List<User>> usersList(@RequestParam(required = false) String login,
                                                @RequestParam(required = false) Status userStatus,
                                                Pageable pageable) {
        OptionalBooleanBuilder predicate = new OptionalBooleanBuilder()
            .notEmptyAnd(qUser.userStatus::eq,userStatus)
            //把没有删除的用户信息展示出来
            //.notEmptyAnd(qUser.userStatus::ne,Status.DELETE)
            .notEmptyAnd(qUser.login::eq,login);
        JPAQuery<User> jpaQuery = queryFactory.selectFrom(qUser)
            .where(predicate.build())
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset());
        Page<User> page = new PageImpl<User>(jpaQuery.fetch(), pageable, jpaQuery.fetchCount());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

/*
通过userId获取用户的详细信息，在展示用户列表时，后面可以添加用户详情按钮
*/
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "用户详情", description = "作者：田春晓")
    @GetMapping("/user/userById")
    public ResponseEntity<User> getUserId(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(user);
    }
/*
管理员启用用户
*/
    @Operation(summary = "启用用户", description = "作者：田春晓")
    @PutMapping("/user/restartUser")
    public ResponseEntity<Void> restartUser(@RequestParam Long userId) {
       userService.restartUser(userId);
       return ResponseEntity.ok().build();
    }
    /*
管理员禁用用户
*/
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "禁用用户", description = "作者：田春晓")
    @PutMapping("/user/disableUser")
    public ResponseEntity<Void> disableUser(@RequestParam Long userId) {
        userService.disableUser(userId);
        return ResponseEntity.ok().build();
    }

/*
管理员初始化密码为123456
*/
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "初始化密码", description = "作者：田春晓")
    @PutMapping("/user/initialUserPassword")
    public ResponseEntity<Void> initialUserPassword(@RequestParam String login) {
        userService.initialUserPassword(login);
        return ResponseEntity.ok().build();
    }
/*
* 患者注册账号,只适用于住院系统里有信息的患者，并且护士没有录入信息的患者
*身份证号用于当前账号与patient表里的住院患者信息相关联。
* 员工如果新增账号，需要由管理员去新增账号并分配角色
 */
@Operation(summary = "注册账号", description = "作者：田春晓")
@PostMapping("/user/register")
public ResponseEntity<Void> register(@RequestParam String login,
                                     @RequestParam String password,
                                     @RequestParam String idNum) {
    Optional<User> userByLogin = userRepository.findOneByLogin(login);
    if(userByLogin.isPresent()){
        throw new BadRequestAlertException("当前用户名账号已存在，请换一个另外的用户名账号","","注册失败");
    }
    if(idNum.length() < 18){
        throw new BadRequestAlertException("您输入的身份证号不满18位，请重新输入","","注册失败");
    }
    User newUser = new User();
    newUser.setLogin(login);
    newUser.setPassword(passwordEncoder.encode(password));
    newUser.setUserStatus(Status.NORMAL);
    userRepository.save(newUser);

    UserRole newUserRole = new UserRole();
    newUserRole.setUserId(newUser.getId());
    Optional<Role> role = roleRepository.findByRoleNameInEn(RoleName.PATIENT);
    newUserRole.setRoleId(role.get().getId());
    userRoleRepository.save(newUserRole);

    OptionalBooleanBuilder predicate = new OptionalBooleanBuilder()
        //去patient表里寻找身份证号相同的patient记录，如果有多次入院则会有多条idNum相同的patient记录
        .notEmptyAnd(qPatient.idNum::eq, idNum);
    JPAQuery<Patient> jpaQuery = queryFactory.selectFrom(qPatient)
        .where(predicate.build())
        //按照时间进行降序排序时，最近最新的时间是 list 的第一个元素
        .orderBy(qPatient.admissionDate.desc());
    //获得最近最新的patient记录
    Optional<Patient> newestPatient = jpaQuery.fetch().stream().findFirst();
    if (!newestPatient.isPresent()){
        throw new BadRequestAlertException("您输入的身份证号无法与患者信息匹配","","注册失败");
    }
    newestPatient.get().setUserId(newUser.getId());
    newestPatient.get().setLogin(newUser.getLogin());
    patientRepository.save(newestPatient.get());


    return ResponseEntity.ok().build();
}






   /* @GetMapping("/findAll")
    @ApiOperation("分页查询")
    public ResponseEntity<List<TbYonghu>> findAll(Pageable pageable) {*/


/*        要求:
            1. 理解代码逻辑
            2. 尝试按照ProductResource内代码进行优化提示信息
         */

        /*Page<TbYonghu> page = tbYonghuRepository.findAll(pageable);
        page.forEach(item -> item.setMima(null));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }*/
/*
    @Transactional
    @PostMapping("/add")
    @ApiOperation("新增用户")
    public ResponseEntity<String> addUser(@RequestParam("name") String name,
                                          @RequestParam("zhanghao") String zhanghao,
                                          @RequestParam("mima") String mima) {
        */
/*
        要求:
            1. 理解代码逻辑
            2. 尝试按照ProductResource内代码进行优化提示信息
         *//*



        Optional<TbYonghu> tbYonghuHaveOpt = tbYonghuRepository.findByZhanghao(zhanghao);
        if (tbYonghuHaveOpt.isPresent()) {
            throw new BadRequestAlertException("账号已存在", "", "添加失败。");
        }
        TbYonghu tbYonghu = new TbYonghu();
        tbYonghu.setName(name);
        tbYonghu.setZhanghao(zhanghao);
        tbYonghu.setMima(passwordEncoder.encode(mima));
        tbYonghu.setCreateTime(TimeUtil.now());
        tbYonghu = tbYonghuRepository.save(tbYonghu);
        return ResponseEntity.ok().body("ok");
    }

    @Transactional
    @GetMapping("/delete")
    @ApiOperation("删除用户")
    public ResponseEntity<String> deleteUser(@RequestParam("id") String id) {
        */
/*
        要求:
            1. 理解代码逻辑
            2. 尝试按照ProductResource内代码进行优化提示信息
         *//*



        if ("1".equals(id)) {
            throw new BadRequestAlertException("admin账户不能删除", "", "删除失败。");
        }
        try {
            tbYonghuRepository.deleteTbYonghuById(Long.parseLong(id));
        } catch (Exception e) {
            throw new BadRequestAlertException("删除失败", "", e.toString());
        }
        return ResponseEntity.ok().body("删除成功");
    }

    @Transactional
    @PostMapping("/update")
    @ApiOperation("修改用户")
    public ResponseEntity<String> updateUser(@RequestParam("id") String id,
                                             @RequestParam("name") String name,
                                             @RequestParam(value = "mima", required = false) String mima) {
        */
/*
        要求:
            1. 理解代码逻辑
            2. 尝试按照ProductResource内代码进行优化提示信息
         *//*



        Optional<TbYonghu> tbYonghuOpt = tbYonghuRepository.findTbYonghuById(Long.parseLong(id));
        if (!tbYonghuOpt.isPresent()) {
            throw new BadRequestAlertException("没有找到用户", "", "修改失败");
        }
        TbYonghu tbYonghu = tbYonghuOpt.get();
        tbYonghu.setName(name);
        if (mima != null) {
            tbYonghu.setMima(passwordEncoder.encode(mima));
        }
        tbYonghuRepository.save(tbYonghu);
        return ResponseEntity.ok().body("修改成功");
    }
*/
}

