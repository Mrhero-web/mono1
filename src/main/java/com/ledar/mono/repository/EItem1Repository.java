package com.ledar.mono.repository;

import com.ledar.mono.domain.EItem1;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EItem1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EItem1Repository extends JpaRepository<EItem1, Long> {}
