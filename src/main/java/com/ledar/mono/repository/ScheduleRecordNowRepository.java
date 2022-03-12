package com.ledar.mono.repository;

import com.ledar.mono.domain.ScheduleRecordNow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ScheduleRecordNow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleRecordNowRepository extends JpaRepository<ScheduleRecordNow, Long> {}
