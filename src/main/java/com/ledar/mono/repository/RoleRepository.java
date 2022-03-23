package com.ledar.mono.repository;

import com.ledar.mono.domain.Role;
import com.ledar.mono.domain.enumeration.RoleName;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Role entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleNameInEn(RoleName roleNameInEn);
}
