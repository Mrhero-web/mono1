package com.ledar.mono.repository;

import com.ledar.mono.domain.UserRole;
import com.ledar.mono.domain.enumeration.Status;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the UserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
        //@Query("select r.roleCode from UserRole ur left join Role r on ur.roleId = r.id where ur.userId = ?1")
         //这俩一样吗？
        @Query("select r.roleCode from Role r left join UserRole ur on r.id = ur.roleId where ur.userId = ?1 ")
        //必须得起别名
        //@Query("select Role.roleCode from Role left join UserRole on Role.id = UserRole.roleId where UserRole.userId = ?1 ")
        List<String> getAllRoleCodeByUserId(Long userId);
        @Query("select r.roleStatus from Role r left join UserRole ur on r.id = ur.roleId where ur.userId = ?1 ")
        List<Status> getAllRoleStatusByUserId(Long userId);

        void deleteAllByUserId(Long userId);

        List<UserRole> findByUserId(Long userId);
}


