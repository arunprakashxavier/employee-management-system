package com.mycompany.employeemanagement.dto; // Use your actual package name

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error; // General error type (e.g., "Bad Request", "Not Found")
    private String message; // More specific error message
    private String path; // The URL path where the error occurred

    // Optional: For validation errors to list specific field issues
    private Map<String, String> validationErrors;

    // Constructor for general errors
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // Constructor for validation errors
    public ErrorResponse(int status, String error, String message, String path, Map<String, String> validationErrors) {
        this(status, error, message, path); // Call the other constructor
        this.validationErrors = validationErrors;
    }

    // Getters are needed for Jackson serialization

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    // Optional Setters if needed, but usually getters are sufficient for response DTOs
}