package com.ledar.mono.security;

import java.util.Collection;

import com.ledar.mono.domain.enumeration.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

public class UserModel extends User {

    private Long id;
    private Status userStatus;
    //private Long employeeId;
//    private String webOrApp;
    //public UserModel(){}
    public UserModel(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Status userStatus) {
        super(username, password, authorities);
        this.id = id;
        this.userStatus = userStatus;

       // this.employeeId = employeeId;
//        this.webOrApp = webOrApp;
    }
//    public UserModel(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    // public Long getEmployeeId() {
    //return employeeId;
    //}

    //public void setEmployeeId(Long employeeId) {
       // this.employeeId = employeeId;
   // }

//    public String getWebOrApp() {
//        return webOrApp;
//    }
//
//    public void setWebOrApp(String webOrApp) {
//        this.webOrApp = webOrApp;
//    }
}
