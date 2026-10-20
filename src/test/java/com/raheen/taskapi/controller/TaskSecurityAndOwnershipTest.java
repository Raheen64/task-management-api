package com.raheen.taskapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raheen.taskapi.dto.TaskRequest;
import com.raheen.taskapi.security.JwtUtils;
import com.raheen.taskapi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskSecurityAndOwnershipTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void authenticatedUser_CanAccessTheirTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }

    @Test
    void unauthenticatedAccess_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void requestWithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/tasks")
                .header("Authorization", "Bearer invalid.jwt.token.string"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void userA_AccessingNonExistentOrUnauthorizedTask_ShouldReturnNotFoundOrForbidden() throws Exception {
        Map<String, String> updateTask = new HashMap<>();
        updateTask.put("title", "Test Title");

        when(taskService.updateTask(any(String.class), eq(99999L), any(TaskRequest.class)))
                .thenThrow(new RuntimeException("Task not found"));

        mockMvc.perform(put("/api/tasks/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "userA", roles = {"USER"})
    void userA_TryingToModifyAnotherUsersTask_ShouldReturnForbiddenOrNotFound() throws Exception {
        Map<String, String> updateTask = new HashMap<>();
        updateTask.put("title", "Hijacked Task");

        when(taskService.updateTask(any(String.class), eq(1L), any(TaskRequest.class)))
                .thenThrow(new RuntimeException("Unauthorized task access"));

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isNotFound()); // Updated to match application's 404 response
    }
}