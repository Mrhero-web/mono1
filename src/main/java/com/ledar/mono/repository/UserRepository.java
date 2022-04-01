package com.ledar.mono.repository;

import com.ledar.mono.domain.User;
import java.util.Optional;

import com.ledar.mono.domain.enumeration.Status;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the User entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLogin(String login);


    Optional<User> findOneByLoginAndUserStatusIsNot(String login, Status delete);

    Optional<User> findByIdAndUserStatusIsNot(Long userId, Status delete);
}
