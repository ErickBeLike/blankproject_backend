package com.application.blank.security.repository;

import com.application.blank.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByUserNameOrEmail(String userName, String email);
}

