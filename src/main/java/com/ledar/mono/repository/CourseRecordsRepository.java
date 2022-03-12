package com.ledar.mono.repository;

import com.ledar.mono.domain.CourseRecords;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CourseRecords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRecordsRepository extends JpaRepository<CourseRecords, Long> {}
