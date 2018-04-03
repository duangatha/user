package com.example.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>
{
    /**
     * @param username
     * @return
     */
    Optional<User> findByUsername(String username);
}