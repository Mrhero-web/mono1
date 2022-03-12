package com.ledar.mono.repository;

import com.ledar.mono.domain.PastStudentRecords;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PastStudentRecords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PastStudentRecordsRepository extends JpaRepository<PastStudentRecords, Long> {}
