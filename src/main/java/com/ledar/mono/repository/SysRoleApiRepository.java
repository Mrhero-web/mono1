package com.ledar.mono.repository;

import com.ledar.mono.domain.SysRoleApi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SysRoleApi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysRoleApiRepository extends JpaRepository<SysRoleApi, Long> {}
