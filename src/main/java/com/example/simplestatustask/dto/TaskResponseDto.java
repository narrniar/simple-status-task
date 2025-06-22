package com.example.simplestatustask.dto;

import com.example.simplestatustask.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Task response object")
public class TaskResponseDto {

    @Schema(description = "Task unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
    private Long id;

    @Schema(description = "Task title", example = "Complete project documentation")
    private String title;

    @Schema(description = "Task description", example = "Write comprehensive documentation for the REST API project")
    private String description;

    @Schema(description = "Task status", example = "PENDING")
    private TaskStatus status;

    @Schema(description = "Task creation timestamp", example = "2025-06-22T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Task last update timestamp", example = "2025-06-22T10:30:00")
    private LocalDateTime updatedAt;
}
