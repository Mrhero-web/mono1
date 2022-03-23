package com.ledar.mono.repository;

import com.ledar.mono.domain.Staff;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Staff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findStaffByLogin(Optional<String> currentUserLogin);
//@Query("select staff.userId from staff where staff.id =?1")
//@Query("select r.roleCode from Role r left join UserRole ur on r.id = ur.roleId where ur.userId = ?1 ")

   // Long findUserIdById(Long staffId);
}
