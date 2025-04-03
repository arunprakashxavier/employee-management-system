# Employee Management System

A full-stack web application for managing employee records, built with Spring Boot and Thymeleaf.

## Description

This project provides a web-based interface and a RESTful API for performing CRUD (Create, Read, Update, Delete) operations on employee data. It features user authentication and role-based authorization, ensuring only administrators can manage employee information. The application includes features like pagination, sorting, and filtering for the employee list displayed in the web UI.

## Features

* **Backend REST API:**
    * CRUD operations for employees (`/api/v1/employees`).
    * Uses Data Transfer Objects (DTOs).
    * Pagination and Sorting support for listing employees.
    * Basic Filtering support (by department).
    * API documentation via Swagger UI (`/swagger-ui.html`).
    * Global exception handling.
* **Web Interface (Thymeleaf):**
    * Secure login page (`/login`).
    * Paginated, sortable, and filterable view of all employees (`/web/employees`).
    * Forms for adding (`/web/employees/new`) and editing (`/web/employees/edit/{id}`) employees with validation.
    * Functionality to delete employees (`/web/employees/delete/{id}`) with confirmation.
    * Read-only view of employee details (`/web/employees/view/{id}`).
    * Custom styling with a background image and enhanced UI elements.
* **Security:**
    * Spring Security integration.
    * Database-backed user authentication.
    * BCrypt password encoding.
    * Role-based authorization (ADMIN role required for all employee management actions).
    * Initial user seeding (admin/user).
* **Testing:**
    * Basic Unit tests for the Service layer.
    * Basic Integration tests for the Controller layer (API).

## Technologies Used

* **Backend:**
    * Java 17 (Amazon Corretto recommended)
    * Spring Boot 3.x (e.g., 3.3.1 - *adjust based on your final version*)
    * Spring Web (MVC, REST Controllers)
    * Spring Data JPA / Hibernate
    * Spring Security
    * MapStruct (for DTO mapping)
    * Validation API (Jakarta Bean Validation)
    * SpringDoc OpenAPI (for Swagger UI)
* **Frontend:**
    * Thymeleaf
    * HTML5
    * CSS3 (including custom styles)
    * Bootstrap 5
    * Bootstrap Icons
* **Database:**
    * MySQL
* **Build Tool:**
    * Maven
* **Testing:**
    * JUnit 5
    * Mockito
    * Spring Boot Test / MockMvc

## Prerequisites

* JDK 17 or later (Amazon Corretto 17 recommended)
* Apache Maven 3.6+
* Git
* MySQL Server (running locally or accessible)
* An IDE (like IntelliJ IDEA, Eclipse/STS, VS Code)

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd employee-management-system
    ```
2.  **Database Setup:**
    * Ensure your MySQL server is running.
    * Connect to MySQL and create the database:
        ```sql
        CREATE DATABASE employee_db;
        ```
    * (Optional but recommended) Create a dedicated user:
        ```sql
        -- Replace 'your_strong_password' with a secure password
        CREATE USER 'emp_user'@'localhost' IDENTIFIED BY 'your_strong_password';
        GRANT ALL PRIVILEGES ON employee_db.* TO 'emp_user'@'localhost';
        FLUSH PRIVILEGES;
        ```
3.  **Configure Application:**
    * Open the `src/main/resources/application.properties` file.
    * Update the following properties to match your MySQL setup:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/employee_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
        spring.datasource.username=emp_user # Or your MySQL username
        spring.datasource.password=your_strong_password # The password you set
        ```
    * **IMPORTANT:** Avoid committing real passwords or sensitive data to Git. Use environment variables or other secure configuration methods for production.
4.  **Build the Project:**
    ```bash
    mvn clean install
    ```
5.  **Run the Application:**
    * Using Maven:
        ```bash
        mvn spring-boot:run
        ```
    * Or, run the `EmployeeManagementSystemApplication` main class directly from your IDE.

## Accessing the Application

* **Web Interface:**
    * Open your browser and navigate to `http://localhost:8080/login`.
    * Log in using the default credentials:
        * Username: `admin` / Password: `password123` (Full access)
        * Username: `user` / Password: `password` (No access to employee management pages by default)
    * After successful admin login, you will be redirected to `http://localhost:8080/web/employees`.
* **REST API (Requires Authentication):**
    * Base Path: `/api/v1/employees`
    * Requires `ADMIN` role for access.
    * Use Basic Authentication (`admin` / `password123`).
    * **Swagger UI:** `http://localhost:8080/swagger-ui.html` (Accessible without login)

## Running Tests

Execute the following Maven command:
```bash
mvn test