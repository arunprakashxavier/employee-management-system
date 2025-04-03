package com.mycompany.employeemanagement.repository;

import com.mycompany.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.data.jpa.repository.JpaRepository;
// Removed JpaSpecificationExecutor for this simple approach
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> { // No change needed here

    Optional<Employee> findByEmail(String email);

    // --- NEW METHOD for filtering by department ---
    // Spring Data JPA automatically creates the query based on the method name
    Page<Employee> findByDepartmentIgnoreCase(String department, Pageable pageable);
    // Using IgnoreCase for case-insensitive matching, which is often user-friendly

    // We could also add findByFirstNameContainingIgnoreCase, findByLastNameContainingIgnoreCase etc. later

}