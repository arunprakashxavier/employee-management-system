package com.mycompany.employeemanagement.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

// Use this DTO for creating employees (POST requests)
// Includes validation annotations relevant to creation
public class EmployeeCreateDTO {

    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber; // Optional

    @PastOrPresent(message = "Hire date cannot be in the future")
    private LocalDate hireDate; // Optional

    @Size(max = 100, message = "Job title must not exceed 100 characters")
    private String jobTitle; // Optional

    @Size(max = 100, message = "Department must not exceed 100 characters")
    private String department; // Optional

    // Getters and Setters (or use Lombok @Data)
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}