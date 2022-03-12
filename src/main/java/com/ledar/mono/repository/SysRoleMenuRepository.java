package com.ledar.mono.repository;

import com.ledar.mono.domain.SysRoleMenu;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SysRoleMenu entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SysRoleMenuRepository extends JpaRepository<SysRoleMenu, Long> {}
