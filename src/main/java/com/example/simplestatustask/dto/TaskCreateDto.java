package com.example.simplestatustask.dto;

import com.example.simplestatustask.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for creating a new task")
public class TaskCreateDto {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Schema(description = "Task title", example = "Complete project documentation", maxLength = 100)
    private String title;

    @Schema(description = "Task description", example = "Write comprehensive documentation for the REST API project")
    private String description;

    @Schema(description = "Task status", example = "PENDING", defaultValue = "PENDING")
    private TaskStatus status = TaskStatus.PENDING;
}
