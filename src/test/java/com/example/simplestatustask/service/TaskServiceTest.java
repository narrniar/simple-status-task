package com.example.simplestatustask.service;

import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskResponseDto;
import com.example.simplestatustask.dto.TaskUpdateDto;
import com.example.simplestatustask.exception.TaskNotFoundException;
import com.example.simplestatustask.mapper.TaskMapper;
import com.example.simplestatustask.models.Task;
import com.example.simplestatustask.repository.TaskRepository;
import com.example.simplestatustask.service.implementation.TaskServiceImplementation;
import com.example.simplestatustask.util.TestDataBuilder;
import com.example.simplestatustask.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for TaskService
 * 
 * This class demonstrates unit testing with Mockito and JUnit 5.
 * 
 * Key Concepts:
 * - @ExtendWith(MockitoExtension.class): Enables Mockito integration with JUnit 5
 * - @Mock: Creates mock objects for dependencies
 * - @InjectMocks: Injects mocks into the class under test
 * - when().thenReturn(): Defines mock behavior
 * - verify(): Verifies that methods were called with expected parameters
 * - assertThrows(): Tests for exceptions
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImplementation taskService;

    private Task sampleTask;
    private TaskResponseDto sampleResponseDto;
    private TaskCreateDto sampleCreateDto;
    private TaskUpdateDto sampleUpdateDto;

    @BeforeEach
    void setUp() {
        // Initialize test data before each test
        sampleTask = TestDataBuilder.createSampleTask();
        sampleResponseDto = TestDataBuilder.createSampleTaskResponseDto();
        sampleCreateDto = TestDataBuilder.createSampleTaskCreateDto();
        sampleUpdateDto = TestDataBuilder.createSampleTaskUpdateDto();
    }

    @Test
    @DisplayName("Should create task successfully")
    void createTask_Success() {
        // Arrange (Given) - Set up test data and mock behavior
        when(taskMapper.toEntity(sampleCreateDto)).thenReturn(sampleTask);
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);
        when(taskMapper.toResponseDto(sampleTask)).thenReturn(sampleResponseDto);

        // Act (When) - Execute the method under test
        TaskResponseDto result = taskService.createTask(sampleCreateDto);

        // Assert (Then) - Verify the results
        assertNotNull(result);
        assertEquals(sampleResponseDto.getId(), result.getId());
        assertEquals(sampleResponseDto.getTitle(), result.getTitle());

        // Verify that the expected methods were called
        verify(taskMapper).toEntity(sampleCreateDto);
        verify(taskRepository).save(sampleTask);
        verify(taskMapper).toResponseDto(sampleTask);
    }

    @Test
    @DisplayName("Should retrieve task by ID successfully")
    void getTaskById_Success() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(sampleTask));
        when(taskMapper.toResponseDto(sampleTask)).thenReturn(sampleResponseDto);

        // Act
        TaskResponseDto result = taskService.getTaskById(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(sampleResponseDto.getId(), result.getId());
        assertEquals(sampleResponseDto.getTitle(), result.getTitle());

        verify(taskRepository).findById(taskId);
        verify(taskMapper).toResponseDto(sampleTask);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when task not found")
    void getTaskById_TaskNotFound() {
        // Arrange
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(taskId)
        );

        assertEquals("Task not found with ID: " + taskId, exception.getMessage());
        verify(taskRepository).findById(taskId);
        verify(taskMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should update task successfully")
    void updateTask_Success() {
        // Arrange
        Long taskId = 1L;
        Task existingTask = TestDataBuilder.createTask(taskId, "Old Title", "Old Description", TaskStatus.PENDING);
        Task updatedTask = TestDataBuilder.createTask(taskId, "New Title", "New Description", TaskStatus.IN_PROGRESS);
        TaskResponseDto updatedResponseDto = TestDataBuilder.createTaskResponseDto(taskId, "New Title", "New Description", TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toResponseDto(updatedTask)).thenReturn(updatedResponseDto);

        // Act
        TaskResponseDto result = taskService.updateTask(taskId, sampleUpdateDto);

        // Assert
        assertNotNull(result);
        assertEquals(updatedResponseDto.getTitle(), result.getTitle());
        assertEquals(updatedResponseDto.getStatus(), result.getStatus());

        verify(taskRepository).findById(taskId);
        verify(taskMapper).updateEntityFromDto(sampleUpdateDto, existingTask);
        verify(taskRepository).save(existingTask);
        verify(taskMapper).toResponseDto(updatedTask);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when updating non-existent task")
    void updateTask_TaskNotFound() {
        // Arrange
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateTask(taskId, sampleUpdateDto)
        );

        assertEquals("Task not found with ID: " + taskId, exception.getMessage());
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any());
        verify(taskMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should delete task successfully")
    void deleteTask_Success() {
        // Arrange
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(taskId);

        // Act
        assertDoesNotThrow(() -> taskService.deleteTask(taskId));

        // Assert
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when deleting non-existent task")
    void deleteTask_TaskNotFound() {
        // Arrange
        Long taskId = 999L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // Act & Assert
        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(taskId)
        );

        assertEquals("Task not found with ID: " + taskId, exception.getMessage());
        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should handle null description in create task")
    void createTask_WithNullDescription() {
        // Arrange
        TaskCreateDto createDtoWithNullDescription = new TaskCreateDto("Task Title", null, TaskStatus.PENDING);
        Task taskWithNullDescription = TestDataBuilder.createTask(1L, "Task Title", null, TaskStatus.PENDING);
        
        when(taskMapper.toEntity(createDtoWithNullDescription)).thenReturn(taskWithNullDescription);
        when(taskRepository.save(any(Task.class))).thenReturn(taskWithNullDescription);
        when(taskMapper.toResponseDto(taskWithNullDescription)).thenReturn(sampleResponseDto);

        // Act
        TaskResponseDto result = taskService.createTask(createDtoWithNullDescription);

        // Assert
        assertNotNull(result);
        verify(taskMapper).toEntity(createDtoWithNullDescription);
        verify(taskRepository).save(taskWithNullDescription);
    }

    @Test
    @DisplayName("Should handle different task statuses")
    void createTask_WithDifferentStatuses() {
        // Test with each status
        TaskStatus[] statuses = {TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.COMPLETED};
        
        for (TaskStatus status : statuses) {
            // Arrange
            TaskCreateDto createDto = TestDataBuilder.createTaskCreateDto("Task", "Description", status);
            Task task = TestDataBuilder.createTask(1L, "Task", "Description", status);
            TaskResponseDto responseDto = TestDataBuilder.createTaskResponseDto(1L, "Task", "Description", status);
            
            when(taskMapper.toEntity(createDto)).thenReturn(task);
            when(taskRepository.save(any(Task.class))).thenReturn(task);
            when(taskMapper.toResponseDto(task)).thenReturn(responseDto);

            // Act
            TaskResponseDto result = taskService.createTask(createDto);

            // Assert
            assertNotNull(result);
            assertEquals(status, result.getStatus());
        }
    }
} 