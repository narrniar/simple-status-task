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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    // Almaty timezone constant
    private static final ZoneId ALMATY_ZONE = ZoneId.of("Asia/Almaty");

    /**
     * Creates a new task with Almaty timezone
     *
     * @param createDto DTO containing task creation data
     * @return Created task as response DTO
     */
    @Override
    public TaskResponseDto createTask(TaskCreateDto createDto) {
        log.info("Creating new task with title: {} at Almaty time: {}",
                createDto.getTitle(), getCurrentAlmatyTime());

        // Convert DTO to entity
        Task task = taskMapper.toEntity(createDto);

        // Set timestamps with Almaty timezone
        LocalDateTime now = getCurrentAlmatyTime();
        task.setCreatedAt(now);
        task.setUpdatedAt(getCurrentAlmatyZonedTime());

        // Save to database
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with ID: {} at Almaty time: {}",
                savedTask.getId(), savedTask.getCreatedAt());

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
        log.info("Retrieving task with ID: {} at Almaty time: {}", id, getCurrentAlmatyTime());

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {} at Almaty time: {}", id, getCurrentAlmatyTime());
                    return new TaskNotFoundException("Task not found with ID: " + id);
                });

        log.info("Task retrieved successfully: {} (created at: {})",
                task.getTitle(), task.getCreatedAt());
        return taskMapper.toResponseDto(task);
    }

    /**
     * Updates an existing task with Almaty timezone
     *
     * @param id Task unique identifier
     * @param updateDto DTO containing updated task data
     * @return Updated task as response DTO
     * @throws TaskNotFoundException if task not found
     */
    @Override
    public TaskResponseDto updateTask(Long id, TaskUpdateDto updateDto) {
        log.info("Updating task with ID: {} at Almaty time: {}", id, getCurrentAlmatyTime());

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Task not found with ID: {} at Almaty time: {}", id, getCurrentAlmatyTime());
                    return new TaskNotFoundException("Task not found with ID: " + id);
                });

        // Update entity with DTO data (only non-null fields)
        taskMapper.updateEntityFromDto(updateDto, existingTask);

        // Update the updatedAt timestamp with Almaty timezone
        existingTask.setUpdatedAt(getCurrentAlmatyZonedTime());

        // Save updated entity
        Task updatedTask = taskRepository.save(existingTask);
        log.info("Task updated successfully with ID: {} at Almaty time: {}",
                updatedTask.getId(), updatedTask.getUpdatedAt());

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
        log.info("Deleting task with ID: {} at Almaty time: {}", id, getCurrentAlmatyTime());

        if (!taskRepository.existsById(id)) {
            log.error("Task not found with ID: {} at Almaty time: {}", id, getCurrentAlmatyTime());
            throw new TaskNotFoundException("Task not found with ID: " + id);
        }

        taskRepository.deleteById(id);
        log.info("Task deleted successfully with ID: {} at Almaty time: {}", id, getCurrentAlmatyTime());
    }

    /**
     * Gets current time in Almaty timezone
     *
     * @return Current LocalDateTime in Almaty timezone
     */
    private LocalDateTime getCurrentAlmatyTime() {
        return LocalDateTime.now(ALMATY_ZONE);
    }

    /**
     * Gets current ZonedDateTime in Almaty timezone
     *
     * @return Current ZonedDateTime in Almaty timezone
     */
    private ZonedDateTime getCurrentAlmatyZonedTime() {
        return ZonedDateTime.now(ALMATY_ZONE);
    }

    /**
     * Converts UTC time to Almaty time
     *
     * @param utcTime UTC LocalDateTime
     * @return LocalDateTime in Almaty timezone
     */
    private LocalDateTime convertUtcToAlmaty(LocalDateTime utcTime) {
        return utcTime.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ALMATY_ZONE)
                .toLocalDateTime();
    }
}