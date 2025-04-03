package com.mycompany.employeemanagement.controller;

import com.mycompany.employeemanagement.dto.EmployeeCreateDTO;
import com.mycompany.employeemanagement.dto.EmployeeResponseDTO;
import com.mycompany.employeemanagement.dto.EmployeeUpdateDTO;
import com.mycompany.employeemanagement.mapper.EmployeeMapper;
import com.mycompany.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger; // Import Logger
import org.slf4j.LoggerFactory; // Import LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/web/employees")
public class EmployeeWebController {

    // Optional: Add logger for catching exceptions
    private static final Logger logger = LoggerFactory.getLogger(EmployeeWebController.class);

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeWebController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.employeeMapper = employeeMapper;
    }

    @GetMapping
    public String listEmployees(
            Model model,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id,asc") String[] sort,
            @RequestParam(name = "department", required = false) String department
    ) {

        // --- Sorting Logic ---
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sortField);

        // --- Pageable Object ---
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // --- Fetch Data ---
        Page<EmployeeResponseDTO> employeePage = employeeService.getAllEmployees(pageable, department);

        // --- Add data to the Model for the view ---
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("departmentFilter", department);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDirection);
        model.addAttribute("reverseSortDir", sortDirection.equals("asc") ? "desc" : "asc");

        // --- Add Page Numbers for Pagination Controls ---
        int totalPages = employeePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "employees-list";
    }

    @GetMapping("/new")
    public String showNewEmployeeForm(Model model) {
        EmployeeCreateDTO employeeDTO = new EmployeeCreateDTO();
        model.addAttribute("employee", employeeDTO);
        model.addAttribute("pageTitle", "Add New Employee");
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") EmployeeCreateDTO employeeDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add New Employee");
            return "employee-form";
        }
        try {
            employeeService.saveEmployee(employeeDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully!");
        } catch (Exception e) {
            logger.error("Error saving new employee", e); // Log exception
            model.addAttribute("errorMessage", "Error saving employee: " + e.getMessage());
            model.addAttribute("pageTitle", "Add New Employee");
            return "employee-form";
        }
        return "redirect:/web/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditEmployeeForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<EmployeeResponseDTO> optionalEmployee = employeeService.getEmployeeById(id);
        if (optionalEmployee.isPresent()) {
            EmployeeUpdateDTO employeeDTO = employeeMapper.employeeResponseDTOToEmployeeUpdateDTO(optionalEmployee.get());
            model.addAttribute("employee", employeeDTO);
            model.addAttribute("employeeId", id);
            model.addAttribute("pageTitle", "Edit Employee (ID: " + id + ")");
            return "employee-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found with ID: " + id);
            return "redirect:/web/employees";
        }
    }

    @PostMapping("/update/{id}")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") EmployeeUpdateDTO employeeDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Employee (ID: " + id + ")");
            model.addAttribute("employeeId", id);
            return "employee-form";
        }
        try {
            Optional<EmployeeResponseDTO> updated = employeeService.updateEmployee(id, employeeDTO);
            if (updated.isPresent()) {
                redirectAttributes.addFlashAttribute("successMessage", "Employee (ID: " + id + ") updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Employee not found with ID: " + id + ", could not update.");
            }
        } catch (Exception e) {
            logger.error("Error updating employee {}", id, e); // Log exception
            model.addAttribute("errorMessage", "Error updating employee: " + e.getMessage());
            model.addAttribute("pageTitle", "Edit Employee (ID: " + id + ")");
            model.addAttribute("employeeId", id);
            return "employee-form";
        }
        return "redirect:/web/employees";
    }

    // --- Method to handle employee deletion ---
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = employeeService.deleteEmployee(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Employee (ID: " + id + ") deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Employee not found with ID: " + id + ", could not delete.");
            }
        } catch (Exception e) {
            logger.error("Error deleting employee {}", id, e); // Log exception
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee (ID: " + id + "). Check for dependencies.");
        }
        return "redirect:/web/employees";
    }

    // --- Method to show employee details view ---
    @GetMapping("/view/{id}")
    public String viewEmployee(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<EmployeeResponseDTO> optionalEmployee = employeeService.getEmployeeById(id);

        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
            model.addAttribute("pageTitle", "View Employee Details (ID: " + id + ")");
            return "employee-view"; // Name of the Thymeleaf template for viewing
        } else {
            // Employee not found, redirect back to list with error message
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found with ID: " + id);
            return "redirect:/web/employees";
        }
    }
}