package com.ledar.mono.repository;

import com.ledar.mono.domain.ScheduleRecordDetailsNow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ScheduleRecordDetailsNow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleRecordDetailsNowRepository extends JpaRepository<ScheduleRecordDetailsNow, Long> {}
