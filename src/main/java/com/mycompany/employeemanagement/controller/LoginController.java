package com.mycompany.employeemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login") // Handle GET requests for /login
    public String login() {
        // Return the name of the Thymeleaf template (login.html)
        // This template should be located in src/main/resources/templates
        return "login";
    }
}