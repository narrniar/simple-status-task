package com.example.simplestatustask.mapper;

import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskResponseDto;
import com.example.simplestatustask.dto.TaskUpdateDto;
import com.example.simplestatustask.models.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    /**
     * Converts Task entity to TaskResponseDto for API responses
     *
     * @param task Task entity to convert
     * @return TaskResponseDto for API response
     */
    TaskResponseDto toResponseDto(Task task);

    /**
     * Converts TaskCreateDto to Task entity for persistence
     * Sets default values for fields not provided in DTO
     *
     * @param createDto DTO containing task creation data
     * @return Task entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", defaultValue = "PENDING")
    Task toEntity(TaskCreateDto createDto);

    /**
     * Updates existing Task entity with data from TaskUpdateDto
     * Only updates non-null fields from the DTO
     *
     * @param updateDto DTO containing updated task data
     * @param task Existing task entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TaskUpdateDto updateDto, @MappingTarget Task task);
}