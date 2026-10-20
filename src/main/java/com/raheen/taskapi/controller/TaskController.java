package com.raheen.taskapi.controller;

import com.raheen.taskapi.dto.TaskRequest;
import com.raheen.taskapi.dto.TaskResponse;
import com.raheen.taskapi.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task for the authenticated user.")
    public ResponseEntity<TaskResponse> createTask(
            Authentication authentication,
            @Valid @RequestBody TaskRequest request) {
        String userEmail = authentication.getName();
        TaskResponse response = taskService.createTask(userEmail, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

   @GetMapping
@Operation(summary = "Get all tasks", description = "Retrieves tasks with optional search title and completion filters.")
public ResponseEntity<Page<TaskResponse>> getAllTasks(
        Principal principal,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Boolean completed,
        @ParameterObject Pageable pageable) {
    Page<TaskResponse> tasks = taskService.getUserTasks(principal.getName(), title, completed, pageable);
    return ResponseEntity.ok(tasks);
}

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing task", description = "Updates task details by ID for the logged-in user.")
    public ResponseEntity<TaskResponse> updateTask(
            Authentication authentication,
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        String userEmail = authentication.getName();
        TaskResponse response = taskService.updateTask(userEmail, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
@Operation(summary = "Delete a task", description = "Deletes a specific task by ID for the logged-in user.")
public ResponseEntity<Void> deleteTask(
        Authentication authentication,
        @PathVariable Long id) {
    String userEmail = authentication.getName();
    taskService.deleteTask(userEmail, id);
    return ResponseEntity.noContent().build(); // Returns 204 No Content
}
}