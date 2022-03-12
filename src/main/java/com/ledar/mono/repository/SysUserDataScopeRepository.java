package com.ledar.mono.repository;

import com.ledar.mono.domain.SysUserDataScope;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SysUserDataScope entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysUserDataScopeRepository extends JpaRepository<SysUserDataScope, Long> {}
