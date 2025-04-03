package com.mycompany.employeemanagement.entity; // Use your actual package name

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users") // Specify table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotEmpty
    @Size(min = 60, max = 60) // BCrypt hashes are typically 60 characters long
    @Column(nullable = false, length = 60)
    private String password;

    @NotEmpty
    @Column(nullable = false)
    private String roles; // Comma-separated roles (e.g., "ROLE_ADMIN,ROLE_USER")

    @Column(nullable = false)
    private boolean enabled = true; // User account status

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}