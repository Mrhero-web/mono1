package com.ledar.mono.repository;

import com.ledar.mono.domain.ScheduleRecordDetailsHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ScheduleRecordDetailsHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleRecordDetailsHistoryRepository extends JpaRepository<ScheduleRecordDetailsHistory, Long> {}
