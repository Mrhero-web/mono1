package com.ledar.mono.repository;

import com.ledar.mono.domain.ARelevanceC;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ARelevanceC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ARelevanceCRepository extends JpaRepository<ARelevanceC, Long> {}
