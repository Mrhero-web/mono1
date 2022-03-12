package com.ledar.mono.repository;

import com.ledar.mono.domain.TreatmentProgram;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TreatmentProgram entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TreatmentProgramRepository extends JpaRepository<TreatmentProgram, Long> {}
