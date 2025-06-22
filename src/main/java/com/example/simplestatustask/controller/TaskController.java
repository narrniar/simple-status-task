package com.example.simplestatustask.controller;

import com.example.simplestatustask.dto.ErrorResponseDto;
import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskResponseDto;
import com.example.simplestatustask.dto.TaskUpdateDto;
import com.example.simplestatustask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Task Management", description = "API for managing tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Creates a new task
     *
     * @param createDto Request body containing task creation data
     * @return Created task with HTTP 201 status
     */
    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskCreateDto createDto) {

        log.info("POST /tasks - Creating new task with title: {}", createDto.getTitle());

        TaskResponseDto createdTask = taskService.createTask(createDto);

        log.info("Task created successfully with ID: {}", createdTask.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Retrieves a task by its ID
     *
     * @param id Task unique identifier
     * @return Task data with HTTP 200 status
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieves a task by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<TaskResponseDto> getTaskById(
            @Parameter(description = "Task unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable Long id) {

        log.info("GET /tasks/{} - Retrieving task", id);

        TaskResponseDto task = taskService.getTaskById(id);

        log.info("Task retrieved successfully: {}", task.getTitle());
        return ResponseEntity.ok(task);
    }

    /**
     * Updates an existing task
     *
     * @param id Task unique identifier
     * @param updateDto Request body containing updated task data
     * @return Updated task with HTTP 200 status
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update task", description = "Updates an existing task with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or UUID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<TaskResponseDto> updateTask(
            @Parameter(description = "Task unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateDto updateDto) {

        log.info("PUT /tasks/{} - Updating task", id);

        TaskResponseDto updatedTask = taskService.updateTask(id, updateDto);

        log.info("Task updated successfully: {}", updatedTask.getTitle());
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Deletes a task by its ID
     *
     * @param id Task unique identifier
     * @return Empty response with HTTP 204 status
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Deletes a task by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "Task unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable Long id) {

        log.info("DELETE /tasks/{} - Deleting task", id);

        taskService.deleteTask(id);

        log.info("Task deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}
