package com.example.simplestatustask.repository;

import com.example.simplestatustask.enums.TaskStatus;
import com.example.simplestatustask.models.Task;
import com.example.simplestatustask.util.TestDataBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository integration tests for TaskRepository
 *
 * - Uses @DataJpaTest for JPA/H2 integration
 * - Each test runs in a transaction and rolls back
 * - Tests CRUD and custom queries
 */
@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Should save and retrieve a task by ID")
    void saveAndFindById() {
        Task task = TestDataBuilder.createTask(null, "Repo Test", "Repo Desc", TaskStatus.PENDING);
        Task saved = taskRepository.save(task);
        Optional<Task> found = taskRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Repo Test");
    }

    @Test
    @DisplayName("Should find tasks by status")
    void findByStatus() {
        Task t1 = taskRepository.save(TestDataBuilder.createTask(null, "A", "", TaskStatus.PENDING));
        Task t2 = taskRepository.save(TestDataBuilder.createTask(null, "B", "", TaskStatus.COMPLETED));
        List<Task> pending = taskRepository.findByStatus(TaskStatus.PENDING);
        assertThat(pending).extracting(Task::getTitle).contains("A");
        assertThat(pending).extracting(Task::getTitle).doesNotContain("B");
    }

    @Test
    @DisplayName("Should find tasks by title containing text (case-insensitive)")
    void findByTitleContainingIgnoreCase() {
        taskRepository.save(TestDataBuilder.createTask(null, "Alpha Task", "", TaskStatus.PENDING));
        taskRepository.save(TestDataBuilder.createTask(null, "Beta Task", "", TaskStatus.PENDING));
        List<Task> found = taskRepository.findByTitleContainingIgnoreCase("alpha");
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("Alpha Task");
    }

    @Test
    @DisplayName("Should find tasks created between two dates")
    void findByCreatedAtBetween() {
        LocalDateTime now = LocalDateTime.now();
        Task t1 = TestDataBuilder.createTask(null, "T1", "", TaskStatus.PENDING);
        t1.setCreatedAt(now.minusDays(2));
        Task t2 = TestDataBuilder.createTask(null, "T2", "", TaskStatus.PENDING);
        t2.setCreatedAt(now.minusDays(1));
        Task t3 = TestDataBuilder.createTask(null, "T3", "", TaskStatus.PENDING);
        t3.setCreatedAt(now);
        taskRepository.saveAll(List.of(t1, t2, t3));
        List<Task> found = taskRepository.findByCreatedAtBetween(now.minusDays(2), now);
        assertThat(found).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should count tasks by status")
    void countByStatus() {
        taskRepository.save(TestDataBuilder.createTask(null, "C1", "", TaskStatus.PENDING));
        taskRepository.save(TestDataBuilder.createTask(null, "C2", "", TaskStatus.PENDING));
        taskRepository.save(TestDataBuilder.createTask(null, "C3", "", TaskStatus.COMPLETED));
        long count = taskRepository.countByStatus(TaskStatus.PENDING);
        assertThat(count).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("Should check if a task exists by title")
    void existsByTitle() {
        taskRepository.save(TestDataBuilder.createTask(null, "UniqueTitle", "", TaskStatus.PENDING));
        boolean exists = taskRepository.existsByTitle("UniqueTitle");
        assertThat(exists).isTrue();
        boolean notExists = taskRepository.existsByTitle("NoSuchTitle");
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should find all tasks ordered by creation date descending")
    void findAllByOrderByCreatedAtDesc() {
        Task t1 = TestDataBuilder.createTask(null, "First", "", TaskStatus.PENDING);
        t1.setCreatedAt(LocalDateTime.now().minusDays(1));
        Task t2 = TestDataBuilder.createTask(null, "Second", "", TaskStatus.PENDING);
        t2.setCreatedAt(LocalDateTime.now());
        taskRepository.saveAll(List.of(t1, t2));
        List<Task> ordered = taskRepository.findAllByOrderByCreatedAtDesc();
        assertThat(ordered.get(0).getTitle()).isEqualTo("Second");
    }

    @Test
    @DisplayName("Should find tasks by status ordered by updated date (custom query)")
    void findTasksByStatusOrderByUpdatedAt() {
        Task t1 = TestDataBuilder.createTask(null, "T1", "", TaskStatus.PENDING);
        t1.setUpdatedAt(LocalDateTime.now().minusHours(2));
        Task t2 = TestDataBuilder.createTask(null, "T2", "", TaskStatus.PENDING);
        t2.setUpdatedAt(LocalDateTime.now());
        taskRepository.saveAll(List.of(t1, t2));
        List<Task> found = taskRepository.findTasksByStatusOrderByUpdatedAt(TaskStatus.PENDING);
        assertThat(found.get(0).getTitle()).isEqualTo("T2");
    }

    @Test
    @DisplayName("Should delete a task by ID")
    void deleteById() {
        Task task = taskRepository.save(TestDataBuilder.createTask(null, "ToDelete", "", TaskStatus.PENDING));
        Long id = task.getId();
        taskRepository.deleteById(id);
        assertThat(taskRepository.findById(id)).isNotPresent();
    }
} 