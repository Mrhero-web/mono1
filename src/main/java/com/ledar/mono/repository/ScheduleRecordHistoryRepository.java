package com.ledar.mono.repository;

import com.ledar.mono.domain.ScheduleRecordHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ScheduleRecordHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleRecordHistoryRepository extends JpaRepository<ScheduleRecordHistory, Long> {}
