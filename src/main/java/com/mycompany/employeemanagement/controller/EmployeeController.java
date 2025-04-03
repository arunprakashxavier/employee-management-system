package com.mycompany.employeemanagement.controller;

import com.mycompany.employeemanagement.dto.EmployeeCreateDTO;
import com.mycompany.employeemanagement.dto.EmployeeResponseDTO;
import com.mycompany.employeemanagement.dto.EmployeeUpdateDTO;
import com.mycompany.employeemanagement.service.EmployeeService;

// Import Page and Pageable
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
// Import annotation for Swagger Pageable documentation
import org.springdoc.core.annotations.ParameterObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import java.util.List; // No longer needed
import java.util.Optional;

@Tag(name = "Employee API", description = "API for managing employee data")
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // --- POST /api/v1/employees ---
    // (No changes needed here)
    @Operation(summary = "Create a new employee", description = "Adds a new employee record to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Parameter(description="Employee details for creation.", required = true,
                    schema=@Schema(implementation = EmployeeCreateDTO.class))
            @Valid @RequestBody EmployeeCreateDTO employeeDTO
    ) {
        EmployeeResponseDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    // --- GET /api/v1/employees --- MODIFIED for Filtering ---
    @Operation(summary = "Get all employees with filtering, pagination and sorting", // <<--- MODIFIED description
            description = "Retrieves a paginated list of employee records. " +
                    "Supports filtering by 'department' (case-insensitive). " + // <<--- ADDED filtering info
                    "Supports sorting by fields like 'id', 'firstName', 'lastName', 'email'. " +
                    "Example: /api/v1/employees?department=IT&page=0&size=10&sort=lastName,asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paged list",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getAllEmployees(
            @ParameterObject Pageable pageable,
            @Parameter(description = "Filter by department name (case-insensitive)", required = false, // <<--- ADDED Swagger parameter doc
                    schema = @Schema(type = "string"))
            @RequestParam(required = false) String department // <<--- ADDED request parameter
    ) {
        // Pass the department filter (can be null) to the service method
        Page<EmployeeResponseDTO> employeePage = employeeService.getAllEmployees(pageable, department); // <<--- Pass department
        return ResponseEntity.ok(employeePage);
    }
    // --- End of Modification ---

    // --- GET /api/v1/employees/{id} ---
    // (No changes needed here)
    @Operation(summary = "Get employee by ID", description = "Retrieves a specific employee record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found with the given ID", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @Parameter(description = "ID of the employee to retrieve", required = true)
            @PathVariable Long id
    ) {
        Optional<EmployeeResponseDTO> optionalEmployee = employeeService.getEmployeeById(id);
        return optionalEmployee
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- PUT /api/v1/employees/{id} ---
    // (No changes needed here)
    @Operation(summary = "Update an existing employee", description = "Updates the details of an existing employee by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data provided", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found with the given ID", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @Parameter(description = "ID of the employee to update", required = true)
            @PathVariable Long id,
            @Parameter(description="Updated employee details.", required = true,
                    schema=@Schema(implementation = EmployeeUpdateDTO.class))
            @Valid @RequestBody EmployeeUpdateDTO employeeDTO
    ) {
        Optional<EmployeeResponseDTO> updatedEmployeeOptional = employeeService.updateEmployee(id, employeeDTO);
        return updatedEmployeeOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // --- DELETE /api/v1/employees/{id} ---
    // (No changes needed here)
    @Operation(summary = "Delete an employee", description = "Deletes an employee record by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Employee not found with the given ID", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "ID of the employee to delete", required = true)
            @PathVariable Long id
    ) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}