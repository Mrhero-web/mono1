package com.ledar.mono.repository;

import com.ledar.mono.domain.SysRoleDataScope;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SysRoleDataScope entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysRoleDataScopeRepository extends JpaRepository<SysRoleDataScope, Long> {}
