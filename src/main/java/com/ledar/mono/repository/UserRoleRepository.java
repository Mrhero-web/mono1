package com.ledar.mono.repository;

import com.ledar.mono.domain.UserRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the UserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
        @Query("select r.roleCode from UserRole ur left join Role r on ur.roleId = r.id where ur.userId = ?1")
        List<String> getAllRoleCodeByUserId(Long userId);
    }


