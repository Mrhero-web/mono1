package com.ledar.mono.repository;

import com.ledar.mono.domain.InMedicalAdvice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InMedicalAdvice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InMedicalAdviceRepository extends JpaRepository<InMedicalAdvice, Long> {}
