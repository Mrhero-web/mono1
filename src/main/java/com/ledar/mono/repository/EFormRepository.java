package com.ledar.mono.repository;

import com.ledar.mono.domain.EForm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EForm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EFormRepository extends JpaRepository<EForm, Long> {}
