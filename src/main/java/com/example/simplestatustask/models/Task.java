package com.example.simplestatustask.models;

import com.example.simplestatustask.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    /**
     * Unique identifier for the task using UUID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
    @SequenceGenerator(name = "task_seq", sequenceName = "task_sequence", allocationSize = 1)
    private Long id;

    /**
     * Title of the task - required field with maximum 100 characters
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * Description of the task - optional field
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Current status of the task
     * Can be PENDING, IN_PROGRESS, or COMPLETED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status;

    /**
     * Timestamp when the task was created
     * Automatically set on entity creation
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the task was last updated
     * Automatically updated on entity modification
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    /**
     * Constructor for creating a new task with title, description and status
     *
     * @param title Task title
     * @param description Task description
     * @param status Task status
     */

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(ZoneId.of("Asia/Almaty"));
        updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now(ZoneId.of("Asia/Almaty"));
    }

}
