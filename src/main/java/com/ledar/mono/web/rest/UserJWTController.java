package com.ledar.mono.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ledar.mono.domain.User;
import com.ledar.mono.domain.UserRole;
import com.ledar.mono.domain.enumeration.Status;
import com.ledar.mono.repository.UserRepository;
import com.ledar.mono.repository.UserRoleRepository;
import com.ledar.mono.security.jwt.JWTFilter;
import com.ledar.mono.security.jwt.TokenProvider;
import com.ledar.mono.web.rest.errors.BadRequestAlertException;
import com.ledar.mono.web.rest.vm.LoginVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "系统管理")
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRoleRepository userRoleRepository;

    public UserJWTController(TokenProvider tokenProvider, UserRepository userRepository, AuthenticationManagerBuilder authenticationManagerBuilder, UserRoleRepository userRoleRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRoleRepository = userRoleRepository;
    }

    @PostMapping("/authenticate")
    @Operation(summary = "登录")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        Optional<User> user = userRepository.findOneByLogin(loginVM.getUsername());
        Long userId = user.get().getId();
        List<Status> roleStatusByUserId = userRoleRepository.getAllRoleStatusByUserId(userId);
        System.out.println(roleStatusByUserId);
        if(!roleStatusByUserId.contains(Status.NORMAL)){
            throw new BadRequestAlertException("当前您想要登录的账号对应的角色不可用","","登录失败");
        }

        if(user.get().getUserStatus().equals(Status.DELETE)){
            throw new BadRequestAlertException("当前您想要登录的账号已被删除","","登录失败");
        }
        if(user.get().getUserStatus().equals(Status.DISABLE)){
            throw new BadRequestAlertException("当前您想要登录的账号已被停用","","登录失败");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

//    @GetMapping("/getCurrentUser-details")
//    @ApiOperation(value = "获取当前登录用户信息")
//    public ResponseEntity<User> getCurrentUser(@RequestParam String userName) {
////        Optional<SysUserTable> sysUserTableOptional = sysUserTableRepository.findOneByLoginCode(login);
//        Optional<User> sysUserOptional = userRepository.findOneByLoginAndStatusIsNot(userName, Status.DELETE);
//        if (!sysUserOptional.isPresent()) {
//            throw new BadRequestAlertException("该用户不存在或已被删除", "", "获取失败");
//        }
//        User result = sysUserOptional.get();
//        return ResponseEntity.ok().body(result);
//    }

    /**
     * Object to return as body in JWT Authentication.
     */

    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
