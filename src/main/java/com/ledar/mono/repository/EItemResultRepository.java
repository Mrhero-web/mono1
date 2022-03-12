package com.ledar.mono.repository;

import com.ledar.mono.domain.EItemResult;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EItemResult entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EItemResultRepository extends JpaRepository<EItemResult, Long> {}
