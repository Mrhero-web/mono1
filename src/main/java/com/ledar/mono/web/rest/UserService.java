package com.ledar.mono.web.rest;

import com.ledar.mono.domain.*;

import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.repository.StaffRepository;
import com.ledar.mono.repository.UserRepository;
import com.ledar.mono.repository.UserRoleRepository;
import com.ledar.mono.security.SecurityUtils;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

//import static com.ledar.mono.domain.enumeration.Status.NORMAL;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService<staffRepository> {
    private static final String DEFAULT_PASSWORD = "123456";
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private  final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, UserRoleRepository userRoleRepository1, StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
     * 新增用户
     *@author 田春晓
     * */
    public User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        //默认密码初始值为123456
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setUserStatus(Status.NORMAL);
        userRepository.save(user);
        return user;
    }
    public void deleteUser(Long userId){
            Optional<User> userOpt = userRepository.findById(userId);
            if(!userOpt.isPresent()){
                throw new BadRequestAlertException("没有您要删除的用户","","无法删除");
            }
            User user = userOpt.get();
            user.setUserStatus(Status.DELETE);
            userRepository.save(user);
//            return user;
        }

    /*
    * 修改当前登录账号的密码
    * @author 田春晓
    * */
    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword){
        SecurityUtils
            .getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new BadRequestAlertException("原密码错误","","更改密码失败");
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);

                    log.debug("Changed password for User: {}", user);
                }
            );
    }
    /**
     * 修改用户的角色
     *
     * @param userId
     * @param roleIdList
     * @author 田春晓
     *
     */
    public void updateUserRole(Long userId, List<Long> roleIdList) {
        if (userId != null && roleIdList != null) {
            //先删除
            QUserRole qUserRole = QUserRole.userRole;
            userRoleRepository.deleteAllByUserId(userId);
            //后新增
            for (Long roleId : roleIdList) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRoleRepository.saveAndFlush(userRole);
            }
        }
        //return userRoleRepository.findByUserId(userId);
    }
    /**
     * 禁用用户
     *
     * @param userId
     *
     * @author 田春晓
     *
     */
    public void disableUser(Long userId){
        Optional<User> userById = userRepository.findById(userId);
        if(userById.get().getUserStatus().equals(Status.DISABLE)){
            throw new BadRequestAlertException("当前用户状态已为禁用状态，不需修改","","修改失败");
        }else{
            userById.get().setUserStatus(Status.DISABLE);
        }
    }
    /**
     * 启用用户
     *
     * @param userId
     *
     * @author 田春晓
     *
     */
    public void restartUser(Long userId){
        Optional<User> userById = userRepository.findById(userId);
        if(userById.get().getUserStatus().equals(Status.NORMAL)){
            throw new BadRequestAlertException("当前用户状态已为启用状态，不需修改","","修改失败");
        }else{
            userById.get().setUserStatus(Status.NORMAL);
        }
    }

    /**
     * 用户忘记密码时可以找管理员初始化密码为123456

     * @param login
     *
     * @author 田春晓
     *
     */
    public void initialUserPassword(String login){
        Optional<User> userBylogin = userRepository.findOneByLogin(login);
        if(!userBylogin.isPresent()){
            throw new BadRequestAlertException("您输入的用户名不存在","","重置密码失败");
        }else{
            userBylogin.get().setPassword(passwordEncoder.encode("123456"));
        }
    }








}
