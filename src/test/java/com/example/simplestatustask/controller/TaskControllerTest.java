package com.example.simplestatustask.controller;

import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskResponseDto;
import com.example.simplestatustask.dto.TaskUpdateDto;
import com.example.simplestatustask.exception.TaskNotFoundException;
import com.example.simplestatustask.service.TaskService;
import com.example.simplestatustask.util.TestDataBuilder;
import com.example.simplestatustask.enums.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller Tests for TaskController
 * 
 * This class demonstrates testing REST controllers using MockMvc.
 * 
 * Key Concepts:
 * - MockMvc: Simulates HTTP requests to test controllers
 * - @WebMvcTest: Alternative annotation for controller-only testing
 * - content().json(): Verifies JSON response content
 * - status(): Verifies HTTP status codes
 * - jsonPath(): Extracts and verifies specific JSON fields
 */
@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private TaskCreateDto sampleCreateDto;
    private TaskUpdateDto sampleUpdateDto;
    private TaskResponseDto sampleResponseDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
        
        sampleCreateDto = TestDataBuilder.createSampleTaskCreateDto();
        sampleUpdateDto = TestDataBuilder.createSampleTaskUpdateDto();
        sampleResponseDto = TestDataBuilder.createSampleTaskResponseDto();
    }

    @Test
    @DisplayName("POST /tasks - Should create task successfully")
    void createTask_Success() throws Exception {
        // Arrange
        when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(sampleResponseDto);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleResponseDto.getId()))
                .andExpect(jsonPath("$.title").value(sampleResponseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(sampleResponseDto.getDescription()))
                .andExpect(jsonPath("$.status").value(sampleResponseDto.getStatus().toString()));

        verify(taskService).createTask(any(TaskCreateDto.class));
    }

    @Test
    @DisplayName("POST /tasks - Should return 400 for invalid input")
    void createTask_InvalidInput() throws Exception {
        // Arrange - Create invalid DTO (empty title)
        TaskCreateDto invalidDto = new TaskCreateDto("", "Description", TaskStatus.PENDING);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @DisplayName("GET /tasks/{id} - Should retrieve task successfully")
    void getTaskById_Success() throws Exception {
        // Arrange
        Long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenReturn(sampleResponseDto);

        // Act & Assert
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleResponseDto.getId()))
                .andExpect(jsonPath("$.title").value(sampleResponseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(sampleResponseDto.getDescription()))
                .andExpect(jsonPath("$.status").value(sampleResponseDto.getStatus().toString()));

        verify(taskService).getTaskById(taskId);
    }

    @Test
    @DisplayName("GET /tasks/{id} - Should return 404 when task not found")
    void getTaskById_NotFound() throws Exception {
        // Arrange
        Long taskId = 999L;
        when(taskService.getTaskById(taskId)).thenThrow(new TaskNotFoundException("Task not found with ID: " + taskId));

        // Act & Assert
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isNotFound());

        verify(taskService).getTaskById(taskId);
    }

    @Test
    @DisplayName("PUT /tasks/{id} - Should update task successfully")
    void updateTask_Success() throws Exception {
        // Arrange
        Long taskId = 1L;
        TaskResponseDto updatedResponseDto = TestDataBuilder.createTaskResponseDto(
                taskId, "Updated Title", "Updated Description", TaskStatus.IN_PROGRESS);
        
        when(taskService.updateTask(eq(taskId), any(TaskUpdateDto.class))).thenReturn(updatedResponseDto);

        // Act & Assert
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(updatedResponseDto.getId()))
                .andExpect(jsonPath("$.title").value(updatedResponseDto.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedResponseDto.getDescription()))
                .andExpect(jsonPath("$.status").value(updatedResponseDto.getStatus().toString()));

        verify(taskService).updateTask(eq(taskId), any(TaskUpdateDto.class));
    }

    @Test
    @DisplayName("PUT /tasks/{id} - Should return 404 when updating non-existent task")
    void updateTask_NotFound() throws Exception {
        // Arrange
        Long taskId = 999L;
        when(taskService.updateTask(eq(taskId), any(TaskUpdateDto.class)))
                .thenThrow(new TaskNotFoundException("Task not found with ID: " + taskId));

        // Act & Assert
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUpdateDto)))
                .andExpect(status().isNotFound());

        verify(taskService).updateTask(eq(taskId), any(TaskUpdateDto.class));
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - Should delete task successfully")
    void deleteTask_Success() throws Exception {
        // Arrange
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        // Act & Assert
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(taskId);
    }

    @Test
    @DisplayName("DELETE /tasks/{id} - Should return 404 when deleting non-existent task")
    void deleteTask_NotFound() throws Exception {
        // Arrange
        Long taskId = 999L;
        doThrow(new TaskNotFoundException("Task not found with ID: " + taskId))
                .when(taskService).deleteTask(taskId);

        // Act & Assert
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNotFound());

        verify(taskService).deleteTask(taskId);
    }

    @Test
    @DisplayName("POST /tasks - Should handle different task statuses")
    void createTask_WithDifferentStatuses() throws Exception {
        // Test with each status
        TaskStatus[] statuses = {TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.COMPLETED};
        
        for (TaskStatus status : statuses) {
            // Arrange
            TaskCreateDto createDto = TestDataBuilder.createTaskCreateDto("Task", "Description", status);
            TaskResponseDto responseDto = TestDataBuilder.createTaskResponseDto(1L, "Task", "Description", status);
            
            when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(responseDto);

            // Act & Assert
            mockMvc.perform(post("/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(status.toString()));
        }
    }

    @Test
    @DisplayName("POST /tasks - Should handle null description")
    void createTask_WithNullDescription() throws Exception {
        // Arrange
        TaskCreateDto createDtoWithNullDescription = new TaskCreateDto("Task Title", null, TaskStatus.PENDING);
        TaskResponseDto responseDto = TestDataBuilder.createTaskResponseDto(1L, "Task Title", null, TaskStatus.PENDING);
        
        when(taskService.createTask(any(TaskCreateDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDtoWithNullDescription)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task Title"))
                .andExpect(jsonPath("$.description").isEmpty());

        verify(taskService).createTask(any(TaskCreateDto.class));
    }

    @Test
    @DisplayName("POST /tasks - Should validate title length")
    void createTask_TitleTooLong() throws Exception {
        // Arrange - Create DTO with title longer than 100 characters
        String longTitle = "A".repeat(101);
        TaskCreateDto invalidDto = new TaskCreateDto(longTitle, "Description", TaskStatus.PENDING);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }

    @Test
    @DisplayName("POST /tasks - Should validate required title")
    void createTask_MissingTitle() throws Exception {
        // Arrange - Create DTO with null title
        TaskCreateDto invalidDto = new TaskCreateDto(null, "Description", TaskStatus.PENDING);

        // Act & Assert
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(taskService, never()).createTask(any());
    }
} 