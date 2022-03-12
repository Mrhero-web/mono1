package com.ledar.mono.repository;

import com.ledar.mono.domain.ScheduleRecord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ScheduleRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleRecordRepository extends JpaRepository<ScheduleRecord, Long> {}
