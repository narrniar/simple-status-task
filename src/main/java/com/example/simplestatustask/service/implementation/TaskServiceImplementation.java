package com.example.simplestatustask.service.implementation;

import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskResponseDto;
import com.example.simplestatustask.dto.TaskUpdateDto;
import com.example.simplestatustask.exception.TaskNotFoundException;
import com.example.simplestatustask.mapper.TaskMapper;
import com.example.simplestatustask.models.Task;
import com.example.simplestatustask.repository.TaskRepository;
import com.example.simplestatustask.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    /**
     * Creates a new task
     *
     * @param createDto DTO containing task creation data
     * @return Created task as response DTO
     */
    @Override
    public TaskResponseDto createTask(TaskCreateDto createDto) {
        log.info("Creating new task with title: {}", createDto.getTitle());

        // Convert DTO to entity
        Task task = taskMapper.toEntity(createDto);

        // Save to database
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with ID: {}", savedTask.getId());

        // Convert entity to response DTO
        return taskMapper.toResponseDto(savedTask);
    }

    /**
     * Retrieves a task by its ID
     *
     * @param id Task unique identifier
     * @return Task as response DTO
     * @throws TaskNotFoundException if task not found
     */
    @Override
    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long id) {
        log.info("Retrieving task with ID: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new TaskNotFoundException("Task not found with ID: " + id);
                });

        log.info("Task retrieved successfully: {}", task.getTitle());
        return taskMapper.toResponseDto(task);
    }

    /**
     * Updates an existing task
     *
     * @param id Task unique identifier
     * @param updateDto DTO containing updated task data
     * @return Updated task as response DTO
     * @throws TaskNotFoundException if task not found
     */
    @Override
    public TaskResponseDto updateTask(Long id, TaskUpdateDto updateDto) {
        log.info("Updating task with ID: {}", id);

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {}", id);
                    return new TaskNotFoundException("Task not found with ID: " + id);
                });

        // Update entity with DTO data (only non-null fields)
        taskMapper.updateEntityFromDto(updateDto, existingTask);

        // Save updated entity
        Task updatedTask = taskRepository.save(existingTask);
        log.info("Task updated successfully with ID: {}", updatedTask.getId());

        return taskMapper.toResponseDto(updatedTask);
    }

    /**
     * Deletes a task by its ID
     *
     * @param id Task unique identifier
     * @throws TaskNotFoundException if task not found
     */
    @Override
    public void deleteTask(Long id) {
        log.info("Deleting task with ID: {}", id);

        if (!taskRepository.existsById(id)) {
            log.error("Task not found with ID: {}", id);
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }

        taskRepository.deleteById(id);
        log.info("Task deleted successfully with ID: {}", id);
    }
}
