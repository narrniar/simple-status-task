package com.example.simplestatustask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response object")
@Builder
public class ErrorResponseDto {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error message", example = "Validation failed")
    private String message;

    @Schema(description = "Detailed error messages", example = "[\"Title is required\", \"Title must not exceed 100 characters\"]")
    private List<String> details;

    @Schema(description = "Error timestamp", example = "2025-06-22T10:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Request path", example = "/api/tasks")
    private String path;

    public ErrorResponseDto(int status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for error response with details
     */
    public ErrorResponseDto(int status, String message, List<String> details, String path) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
