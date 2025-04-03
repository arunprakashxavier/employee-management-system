package com.mycompany.employeemanagement.controller;

import com.mycompany.employeemanagement.dto.EmployeeResponseDTO;
import com.mycompany.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller; // Use @Controller for MVC
import org.springframework.ui.Model; // To pass data to the view
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller // Marks this as a Spring MVC controller (serves views)
@RequestMapping("/web/employees") // Base path for web employee operations
public class EmployeeWebController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeWebController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public String listEmployees(
            Model model, // Spring injects the Model object
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "id,asc") String[] sort, // Default sort by id asc
            @RequestParam(name = "department", required = false) String department // Optional department filter
    ) {

        // --- Sorting Logic ---
        // Handle multi-column sort parameters like "lastName,asc", "firstName,desc"
        String sortField = sort[0];
        String sortDirection = sort.length > 1 ? sort[1] : "asc"; // Default direction is asc
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sortObj = Sort.by(direction, sortField);
        // Consider handling multiple sort parameters if needed

        // --- Pageable Object ---
        Pageable pageable = PageRequest.of(page, size, sortObj);

        // --- Fetch Data ---
        Page<EmployeeResponseDTO> employeePage = employeeService.getAllEmployees(pageable, department);

        // --- Add data to the Model for the view ---
        model.addAttribute("employeePage", employeePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("departmentFilter", department); // Pass filter back to view
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

        // --- Return Logical View Name ---
        // Corresponds to src/main/resources/templates/employees-list.html
        return "employees-list";
    }
}