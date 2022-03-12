package com.ledar.mono.repository;

import com.ledar.mono.domain.ScheduleRecordDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ScheduleRecordDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleRecordDetailsRepository extends JpaRepository<ScheduleRecordDetails, Long> {}
