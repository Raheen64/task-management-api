package com.raheen.taskapi.service;

import com.raheen.taskapi.dto.TaskRequest;
import com.raheen.taskapi.dto.TaskResponse;
import com.raheen.taskapi.entity.Task;
import com.raheen.taskapi.entity.User;
import com.raheen.taskapi.repository.TaskRepository;
import com.raheen.taskapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }

    public TaskResponse createTask(String userEmail, TaskRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.isCompleted());
        task.setUser(user);

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    public Page<TaskResponse> getUserTasks(String userEmail, String title, Boolean completed, Pageable pageable) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

    Page<Task> tasks;
    if (title != null && !title.isBlank() && completed != null) {
        tasks = taskRepository.findByUserAndTitleContainingIgnoreCaseAndCompleted(user, title, completed, pageable);
    } else if (title != null && !title.isBlank()) {
        tasks = taskRepository.findByUserAndTitleContainingIgnoreCase(user, title, pageable);
    } else if (completed != null) {
        tasks = taskRepository.findByUserAndCompleted(user, completed, pageable);
    } else {
        tasks = taskRepository.findByUser(user, pageable);
    }

    return tasks.map(this::mapToResponse);
}

    public void deleteTask(String userEmail, Long taskId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Task task = taskRepository.findByIdAndUser(taskId, user)
                .orElseThrow(() -> new RuntimeException("Task not found or you do not have permission to delete it"));

        taskRepository.delete(task);
    }

    public TaskResponse updateTask(String userEmail, Long taskId, TaskRequest request) {
    User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

    Task task = taskRepository.findByIdAndUser(taskId, user)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

    // Update task details
    task.setTitle(request.getTitle());
    task.setDescription(request.getDescription());
    task.setCompleted(request.isCompleted()); // Or status update

    Task updatedTask = taskRepository.save(task);
    return mapToResponse(updatedTask);
}

}