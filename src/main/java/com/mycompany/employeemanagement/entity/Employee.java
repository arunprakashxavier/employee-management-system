package com.mycompany.employeemanagement.entity; // Use your actual package name

import jakarta.persistence.*; // JPA annotations
import jakarta.validation.constraints.*; // Validation annotations
import java.time.LocalDate;

@Entity // Marks this class as a JPA entity (will be mapped to a database table)
@Table(name = "employees") // Specifies the table name in the database
public class Employee {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the ID generation strategy (auto-increment for MySQL)
    private Long id;

    @NotEmpty(message = "First name cannot be empty") // Validation: cannot be null or empty string
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters") // Validation: size constraint
    @Column(name = "first_name", nullable = false, length = 50) // Maps to 'first_name' column, not nullable, max length 50
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid") // Validation: must be a valid email format
    @Column(name = "email", nullable = false, unique = true, length = 100) // Maps to 'email' column, must be unique
    private String email;

    @Column(name = "phone_number", length = 20) // Optional field
    private String phoneNumber;

    @PastOrPresent(message = "Hire date cannot be in the future") // Validation: date must be in the past or present
    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "job_title", length = 100)
    private String jobTitle;

    @Column(name = "department", length = 100)
    private String department;

    // --- Getters and Setters ---
    // Required by JPA and frameworks like Jackson (for JSON conversion)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // --- toString() method (Optional but helpful for logging/debugging) ---
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", hireDate=" + hireDate +
                ", jobTitle='" + jobTitle + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}