package com.mycompany.employeemanagement.service;

import com.mycompany.employeemanagement.dto.EmployeeCreateDTO;
import com.mycompany.employeemanagement.dto.EmployeeResponseDTO;
import com.mycompany.employeemanagement.dto.EmployeeUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService {

    EmployeeResponseDTO saveEmployee(EmployeeCreateDTO employeeDTO);

    // Modified: Accept optional department filter
    Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable, String department);

    Optional<EmployeeResponseDTO> getEmployeeById(Long id);

    Optional<EmployeeResponseDTO> updateEmployee(Long id, EmployeeUpdateDTO employeeDTO);

    boolean deleteEmployee(Long id);

    Optional<EmployeeResponseDTO> getEmployeeByEmail(String email);
}