package com.ledar.mono.repository;

import com.ledar.mono.domain.Department;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    //Integer findDepartmentIdBydName(Optional<String> departmentName);
    @Query("select d.id  from Department d where d.dName = ?1")
    Integer findDepartmentIdBydName(String departmentName);
}

