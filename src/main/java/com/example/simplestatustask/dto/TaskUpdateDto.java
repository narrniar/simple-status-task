package com.example.simplestatustask.dto;

import com.example.simplestatustask.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request body for updating an existing task")
public class TaskUpdateDto {

    @Size(max = 100, message = "Title must not exceed 100 characters")
    @Schema(description = "Task title", example = "Updated task title", maxLength = 100)
    private String title;

    @Schema(description = "Task description", example = "Updated task description")
    private String description;

    @Schema(description = "Task status", example = "IN_PROGRESS")
    private TaskStatus status;
}
