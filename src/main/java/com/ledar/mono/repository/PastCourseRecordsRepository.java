package com.ledar.mono.repository;

import com.ledar.mono.domain.PastCourseRecords;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PastCourseRecords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PastCourseRecordsRepository extends JpaRepository<PastCourseRecords, Long> {}
