package com.ledar.mono.repository;

import com.ledar.mono.domain.Classroom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Classroom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {}
