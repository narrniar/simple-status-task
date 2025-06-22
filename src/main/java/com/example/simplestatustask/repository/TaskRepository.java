package com.example.simplestatustask.repository;

import com.example.simplestatustask.enums.TaskStatus;
import com.example.simplestatustask.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Find tasks by status
     *
     * @param status Task status to filter by
     * @return List of tasks with the specified status
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Find tasks by title containing specific text (case-insensitive)
     *
     * @param title Text to search for in task titles
     * @return List of tasks containing the specified text in title
     */
    List<Task> findByTitleContainingIgnoreCase(String title);

    /**
     * Find tasks created between two dates
     *
     * @param startDate Start date for the range
     * @param endDate End date for the range
     * @return List of tasks created within the specified date range
     */
    List<Task> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Count tasks by status
     *
     * @param status Task status to count
     * @return Number of tasks with the specified status
     */
    long countByStatus(TaskStatus status);

    /**
     * Check if a task exists with the given title
     *
     * @param title Task title to check
     * @return true if a task with the title exists, false otherwise
     */
    boolean existsByTitle(String title);

    /**
     * Find tasks ordered by creation date descending
     *
     * @return List of tasks ordered by creation date (newest first)
     */
    List<Task> findAllByOrderByCreatedAtDesc();

    /**
     * Custom query to find tasks by status with pagination-friendly approach
     *
     * @param status Task status to filter by
     * @return List of tasks with the specified status ordered by updated date
     */
    @Query("SELECT t FROM Task t WHERE t.status = :status ORDER BY t.updatedAt DESC")
    List<Task> findTasksByStatusOrderByUpdatedAt(@Param("status") TaskStatus status);
}
