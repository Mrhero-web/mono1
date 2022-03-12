package com.ledar.mono.repository;

import com.ledar.mono.domain.EItem2;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EItem2 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EItem2Repository extends JpaRepository<EItem2, Long> {}
