package com.mycompany.employeemanagement.service.impl;

import com.mycompany.employeemanagement.dto.EmployeeCreateDTO;
import com.mycompany.employeemanagement.dto.EmployeeResponseDTO;
import com.mycompany.employeemanagement.entity.Employee;
import com.mycompany.employeemanagement.mapper.EmployeeMapper;
import com.mycompany.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks; // Injects mocks into the class under test
import org.mockito.Mock; // Creates mock objects
import org.mockito.junit.jupiter.MockitoExtension; // Integrates Mockito with JUnit 5
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5 assertions
import static org.mockito.ArgumentMatchers.any; // Mockito argument matchers
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given; // BDD style mocking syntax
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enable Mockito extension for JUnit 5
class EmployeeServiceImplTest {

    @Mock // Create a mock EmployeeRepository
    private EmployeeRepository employeeRepository;

    @Mock // Create a mock EmployeeMapper
    private EmployeeMapper employeeMapper;

    @InjectMocks // Create an instance of EmployeeServiceImpl and inject the mocks into it
    private EmployeeServiceImpl employeeService;

    // Sample data for testing
    private Employee employee;
    private EmployeeResponseDTO employeeResponseDTO;
    private EmployeeCreateDTO employeeCreateDTO;

    @BeforeEach // Method runs before each test method
    void setUp() {
        // Initialize sample data
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Arun");
        employee.setLastName("Kumar");
        employee.setEmail("arun.kumar@example.com");
        employee.setDepartment("IT");
        employee.setHireDate(LocalDate.now());

        employeeResponseDTO = new EmployeeResponseDTO();
        employeeResponseDTO.setId(1L);
        employeeResponseDTO.setFirstName("Arun");
        employeeResponseDTO.setLastName("Kumar");
        employeeResponseDTO.setEmail("arun.kumar@example.com");
        employeeResponseDTO.setDepartment("IT");

        employeeCreateDTO = new EmployeeCreateDTO();
        employeeCreateDTO.setFirstName("Arun");
        employeeCreateDTO.setLastName("Kumar");
        employeeCreateDTO.setEmail("arun.kumar@example.com");
        employeeCreateDTO.setDepartment("IT");
    }

    @Test
    @DisplayName("Test Save Employee - Success")
    void givenEmployeeCreateDTO_whenSaveEmployee_thenReturnEmployeeResponseDTO() {
        // Given: Define mock behavior
        given(employeeMapper.employeeCreateDTOToEmployee(any(EmployeeCreateDTO.class))).willReturn(employee);
        given(employeeRepository.save(any(Employee.class))).willReturn(employee); // Assume repository saves and returns the entity
        given(employeeMapper.employeeToEmployeeResponseDTO(any(Employee.class))).willReturn(employeeResponseDTO);

        // When: Call the service method
        EmployeeResponseDTO savedDto = employeeService.saveEmployee(employeeCreateDTO);

        // Then: Assert the results
        assertNotNull(savedDto);
        assertEquals("Arun", savedDto.getFirstName());
        assertEquals(1L, savedDto.getId());

        // Verify that repository.save was called exactly once
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("Test Get Employee By Id - Found")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeResponseDTO() {
        // Given
        Long employeeId = 1L;
        given(employeeRepository.findById(employeeId)).willReturn(Optional.of(employee));
        given(employeeMapper.employeeToEmployeeResponseDTO(employee)).willReturn(employeeResponseDTO);

        // When
        Optional<EmployeeResponseDTO> result = employeeService.getEmployeeById(employeeId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(employeeId, result.get().getId());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    @DisplayName("Test Get Employee By Id - Not Found")
    void givenNonExistentEmployeeId_whenGetEmployeeById_thenReturnEmptyOptional() {
        // Given
        Long employeeId = 99L;
        given(employeeRepository.findById(employeeId)).willReturn(Optional.empty());
        // No need to mock mapper here as it won't be called

        // When
        Optional<EmployeeResponseDTO> result = employeeService.getEmployeeById(employeeId);

        // Then
        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeMapper, never()).employeeToEmployeeResponseDTO(any()); // Verify mapper was NOT called
    }

    @Test
    @DisplayName("Test Get All Employees - No Filter")
    void givenPageable_whenGetAllEmployeesWithoutFilter_thenReturnPageOfDTOs() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> employeePage = new PageImpl<>(List.of(employee), pageable, 1); // Create a page with one employee

        given(employeeRepository.findAll(pageable)).willReturn(employeePage);
        given(employeeMapper.employeeToEmployeeResponseDTO(employee)).willReturn(employeeResponseDTO);

        // When
        Page<EmployeeResponseDTO> resultPage = employeeService.getAllEmployees(pageable, null); // No department filter

        // Then
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(1, resultPage.getContent().size());
        assertEquals("Arun", resultPage.getContent().get(0).getFirstName());
        verify(employeeRepository, times(1)).findAll(pageable);
        verify(employeeRepository, never()).findByDepartmentIgnoreCase(anyString(), any(Pageable.class)); // Ensure filter method wasn't called
    }

    @Test
    @DisplayName("Test Get All Employees - With Filter")
    void givenPageableAndDepartment_whenGetAllEmployeesWithFilter_thenReturnFilteredPageOfDTOs() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String department = "IT";
        Page<Employee> employeePage = new PageImpl<>(List.of(employee), pageable, 1); // Assume filter returns this page

        given(employeeRepository.findByDepartmentIgnoreCase(department, pageable)).willReturn(employeePage);
        given(employeeMapper.employeeToEmployeeResponseDTO(employee)).willReturn(employeeResponseDTO);

        // When
        Page<EmployeeResponseDTO> resultPage = employeeService.getAllEmployees(pageable, department); // With department filter

        // Then
        assertNotNull(resultPage);
        assertEquals(1, resultPage.getTotalElements());
        assertEquals(1, resultPage.getContent().size());
        assertEquals("IT", resultPage.getContent().get(0).getDepartment()); // Check department if relevant
        verify(employeeRepository, times(1)).findByDepartmentIgnoreCase(department, pageable);
        verify(employeeRepository, never()).findAll(any(Pageable.class)); // Ensure non-filter method wasn't called
    }


    @Test
    @DisplayName("Test Delete Employee - Success")
    void givenExistingEmployeeId_whenDeleteEmployee_thenReturnsTrue() {
        // Given
        Long employeeId = 1L;
        given(employeeRepository.existsById(employeeId)).willReturn(true);
        // Mock void method deleteById - BDDMockito.willDoNothing() is clearer than Mockito.doNothing()
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // When
        boolean result = employeeService.deleteEmployee(employeeId);

        // Then
        assertTrue(result);
        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId); // Verify delete was called
    }

    @Test
    @DisplayName("Test Delete Employee - Not Found")
    void givenNonExistentEmployeeId_whenDeleteEmployee_thenReturnsFalse() {
        // Given
        Long employeeId = 99L;
        given(employeeRepository.existsById(employeeId)).willReturn(false);
        // deleteById should not be called

        // When
        boolean result = employeeService.deleteEmployee(employeeId);

        // Then
        assertFalse(result);
        verify(employeeRepository, times(1)).existsById(employeeId);
        verify(employeeRepository, never()).deleteById(anyLong()); // Verify delete was NOT called
    }


}