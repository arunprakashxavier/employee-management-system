package com.mycompany.employeemanagement.security; // Use your actual package name

import com.mycompany.employeemanagement.entity.User;
import com.mycompany.employeemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service // Register this as a Spring service bean
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true) // Good practice for read operations
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Find the user by username from the repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

        // 2. Convert the roles string (e.g., "ROLE_ADMIN,ROLE_USER") into a collection of GrantedAuthority
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(user.getRoles().split(",")) // Split roles string by comma
                        .map(String::trim) // Remove leading/trailing whitespace
                        .filter(role -> !role.isEmpty()) // Filter out empty strings
                        .map(SimpleGrantedAuthority::new) // Create SimpleGrantedAuthority objects
                        .collect(Collectors.toList());

        // 3. Create and return a Spring Security UserDetails object
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Password should be encoded in the database
                user.isEnabled(),   // Use the enabled flag from the User entity
                true,               // accountNonExpired
                true,               // credentialsNonExpired
                true,               // accountNonLocked
                authorities);       // The collection of granted authorities (roles)
    }
}