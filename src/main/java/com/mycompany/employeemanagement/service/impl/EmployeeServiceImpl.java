package com.mycompany.employeemanagement.service.impl;

// Import necessary classes
import com.mycompany.employeemanagement.dto.EmployeeCreateDTO;
import com.mycompany.employeemanagement.dto.EmployeeResponseDTO;
import com.mycompany.employeemanagement.dto.EmployeeUpdateDTO;
import com.mycompany.employeemanagement.entity.Employee;
import com.mycompany.employeemanagement.mapper.EmployeeMapper;
import com.mycompany.employeemanagement.repository.EmployeeRepository;
import com.mycompany.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // Import Page
import org.springframework.data.domain.PageImpl; // Import PageImpl
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // <<--- ADDED import for StringUtils

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeResponseDTO saveEmployee(EmployeeCreateDTO employeeDTO) {
        Employee employee = employeeMapper.employeeCreateDTOToEmployee(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeResponseDTO(savedEmployee);
    }

    // --- MODIFIED to include department filter ---
    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDTO> getAllEmployees(Pageable pageable, String department) { // <<--- ADDED department parameter
        Page<Employee> employeePage;

        // Check if department filter is provided and not empty/blank
        if (StringUtils.hasText(department)) {
            // Call the new repository method if filter is present
            employeePage = employeeRepository.findByDepartmentIgnoreCase(department, pageable);
        } else {
            // Otherwise, call the regular findAll
            employeePage = employeeRepository.findAll(pageable);
        }

        // Map the content (List<Employee>) to List<EmployeeResponseDTO>
        List<EmployeeResponseDTO> dtoList = employeePage.getContent().stream()
                .map(employeeMapper::employeeToEmployeeResponseDTO)
                .collect(Collectors.toList());

        // Create a new Page<EmployeeResponseDTO>
        // Need to provide the mapped list, original pageable, and total elements
        return new PageImpl<>(dtoList, pageable, employeePage.getTotalElements());
    }
    // --- End of Modification ---

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.map(employeeMapper::employeeToEmployeeResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeResponseDTO> getEmployeeByEmail(String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        return optionalEmployee.map(employeeMapper::employeeToEmployeeResponseDTO);
    }


    @Override
    @Transactional
    public Optional<EmployeeResponseDTO> updateEmployee(Long id, EmployeeUpdateDTO employeeDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            employeeMapper.updateEmployeeFromDto(employeeDTO, existingEmployee);
            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            return Optional.of(employeeMapper.employeeToEmployeeResponseDTO(updatedEmployee));
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public boolean deleteEmployee(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}