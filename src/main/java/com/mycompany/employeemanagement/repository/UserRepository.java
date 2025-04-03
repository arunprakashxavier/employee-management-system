package com.mycompany.employeemanagement.repository; // Use your actual package name

import com.mycompany.employeemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Method needed by UserDetailsService to find a user by their username
    Optional<User> findByUsername(String username);

    // Optional: Check if a username already exists
    // boolean existsByUsername(String username);
}