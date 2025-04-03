package com.mycompany.employeemanagement.mapper; // Use your actual package name

import com.mycompany.employeemanagement.dto.EmployeeCreateDTO;
import com.mycompany.employeemanagement.dto.EmployeeResponseDTO;
import com.mycompany.employeemanagement.dto.EmployeeUpdateDTO;
import com.mycompany.employeemanagement.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
// Consider importing Page if you add Page mapping helpers later
// import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring") // Integrates with Spring for dependency injection
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class); // Optional: Instance for non-Spring use

    // --- Entity to DTO ---
    EmployeeResponseDTO employeeToEmployeeResponseDTO(Employee employee);

    List<EmployeeResponseDTO> employeesToEmployeeResponseDTOs(List<Employee> employees);

    // --- DTO to Entity ---
    Employee employeeCreateDTOToEmployee(EmployeeCreateDTO dto);

    // This one might not be strictly needed if updateEmployeeFromDto is always used
    // Employee employeeUpdateDTOToEmployee(EmployeeUpdateDTO dto);

    // --- Update existing Entity from DTO ---
    // This helps apply updates without creating a new entity instance
    // We ignore the 'id' field from the DTO when updating an existing entity
    @Mapping(target = "id", ignore = true)
    void updateEmployeeFromDto(EmployeeUpdateDTO dto, @MappingTarget Employee employee);

    // --- ADDED: DTO to DTO Mapping ---
    // Map ResponseDTO -> UpdateDTO (useful for pre-populating edit form)
    EmployeeUpdateDTO employeeResponseDTOToEmployeeUpdateDTO(EmployeeResponseDTO dto);

}