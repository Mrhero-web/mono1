package com.ledar.mono.repository;

import com.ledar.mono.domain.StudentRecords;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StudentRecords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StudentRecordsRepository extends JpaRepository<StudentRecords, Long> {}
