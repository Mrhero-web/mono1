package com.ledar.mono.repository;

import com.ledar.mono.domain.OutMedicalAdvice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the OutMedicalAdvice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutMedicalAdviceRepository extends JpaRepository<OutMedicalAdvice, Long> {}
