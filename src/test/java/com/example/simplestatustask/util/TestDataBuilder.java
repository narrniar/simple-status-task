package com.example.simplestatustask.util;

import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskResponseDto;
import com.example.simplestatustask.dto.TaskUpdateDto;
import com.example.simplestatustask.enums.TaskStatus;
import com.example.simplestatustask.models.Task;

import java.time.LocalDateTime;

/**
 * Test Data Builder utility class
 * 
 * This class provides methods to create test data objects consistently across all tests.
 * It follows the Builder pattern to make test data creation more readable and maintainable.
 */
public class TestDataBuilder {

    /**
     * Creates a sample Task entity for testing
     */
    public static Task createSampleTask() {
        return Task.builder()
                .id(1L)
                .title("Sample Task")
                .description("This is a sample task for testing")
                .status(TaskStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a Task entity with custom values
     */
    public static Task createTask(Long id, String title, String description, TaskStatus status) {
        return Task.builder()
                .id(id)
                .title(title)
                .description(description)
                .status(status)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Creates a sample TaskCreateDto for testing
     */
    public static TaskCreateDto createSampleTaskCreateDto() {
        return new TaskCreateDto(
                "New Task",
                "This is a new task description",
                TaskStatus.PENDING
        );
    }

    /**
     * Creates a TaskCreateDto with custom values
     */
    public static TaskCreateDto createTaskCreateDto(String title, String description, TaskStatus status) {
        return new TaskCreateDto(title, description, status);
    }

    /**
     * Creates a sample TaskUpdateDto for testing
     */
    public static TaskUpdateDto createSampleTaskUpdateDto() {
        return new TaskUpdateDto(
                "Updated Task",
                "This is an updated task description",
                TaskStatus.IN_PROGRESS
        );
    }

    /**
     * Creates a TaskUpdateDto with custom values
     */
    public static TaskUpdateDto createTaskUpdateDto(String title, String description, TaskStatus status) {
        return new TaskUpdateDto(title, description, status);
    }

    /**
     * Creates a sample TaskResponseDto for testing
     */
    public static TaskResponseDto createSampleTaskResponseDto() {
        return new TaskResponseDto(
                1L,
                "Sample Task",
                "This is a sample task description",
                TaskStatus.PENDING,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    /**
     * Creates a TaskResponseDto with custom values
     */
    public static TaskResponseDto createTaskResponseDto(Long id, String title, String description, TaskStatus status) {
        return new TaskResponseDto(
                id,
                title,
                description,
                status,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
} 