package com.ledar.mono.repository;

import com.ledar.mono.domain.ListOfCourses;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ListOfCourses entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ListOfCoursesRepository extends JpaRepository<ListOfCourses, Long> {}
