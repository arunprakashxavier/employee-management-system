package com.mycompany.employeemanagement.config; // Or a 'runner' package

import com.mycompany.employeemanagement.entity.User;
import com.mycompany.employeemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component // Make it a Spring bean so it gets executed
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Checking for initial admin user...");

        // Check if admin user already exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            logger.info("Admin user not found, creating initial admin user...");

            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("password123")); // Encode the password!
            adminUser.setRoles("ROLE_ADMIN,ROLE_USER"); // Assign roles (use ROLE_ prefix convention)
            adminUser.setEnabled(true);

            userRepository.save(adminUser);
            logger.info("Initial admin user created successfully.");
        } else {
            logger.info("Admin user already exists.");
        }

        // Optionally create a regular user as well
        if (userRepository.findByUsername("user").isEmpty()) {
            logger.info("Regular user not found, creating initial user...");
            User regularUser = new User();
            regularUser.setUsername("user");
            regularUser.setPassword(passwordEncoder.encode("password"));
            regularUser.setRoles("ROLE_USER");
            regularUser.setEnabled(true);
            userRepository.save(regularUser);
            logger.info("Initial regular user created successfully.");
        } else {
            logger.info("Regular user already exists.");
        }
    }
}