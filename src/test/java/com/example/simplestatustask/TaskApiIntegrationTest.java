package com.example.simplestatustask;

import com.example.simplestatustask.dto.TaskCreateDto;
import com.example.simplestatustask.dto.TaskUpdateDto;
import com.example.simplestatustask.enums.TaskStatus;
import com.example.simplestatustask.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Full integration test for the Task API
 *
 * - Uses @SpringBootTest to load the full context
 * - Uses @AutoConfigureMockMvc for HTTP request simulation
 * - No mocks: real beans and H2 database
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Full Task API flow: create, get, update, delete")
    void taskApiFullFlow() throws Exception {
        // --- Create Task ---
        TaskCreateDto createDto = new TaskCreateDto("Integration Task", "Integration Desc", TaskStatus.PENDING);
        MvcResult createResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andReturn();
        // Extract ID from response
        String responseJson = createResult.getResponse().getContentAsString();
        Long taskId = objectMapper.readTree(responseJson).get("id").asLong();

        // --- Get Task ---
        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Integration Task"));

        // --- Update Task ---
        TaskUpdateDto updateDto = new TaskUpdateDto("Updated Title", "Updated Desc", TaskStatus.IN_PROGRESS);
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        // --- Delete Task ---
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNoContent());

        // --- Verify Deletion ---
        assertThat(taskRepository.findById(taskId)).isNotPresent();
    }
} 