package com.example.simplestatustask.service;

import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskResponseDto;
import com.example.simplestatustask.dto.TaskUpdateDto;

public interface TaskService {

    /**
     * Creates a new task
     *
     * @param createDto DTO containing task creation data
     * @return Created task as response DTO
     */
    TaskResponseDto createTask(TaskCreateDto createDto);

    /**
     * Retrieves a task by its ID
     *
     * @param id Task unique identifier
     * @return Task as response DTO
     * @throws com.example.taskapi.exception.TaskNotFoundException if task not found
     */
    TaskResponseDto getTaskById(Long id);

    /**
     * Updates an existing task
     *
     * @param id Task unique identifier
     * @param updateDto DTO containing updated task data
     * @return Updated task as response DTO
     * @throws com.example.taskapi.exception.TaskNotFoundException if task not found
     */
    TaskResponseDto updateTask(Long id, TaskUpdateDto updateDto);

    /**
     * Deletes a task by its ID
     *
     * @param id Task unique identifier
     * @throws com.example.taskapi.exception.TaskNotFoundException if task not found
     */
    void deleteTask(Long id);
}
