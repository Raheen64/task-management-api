package com.raheen.taskapi.repository;

import com.raheen.taskapi.entity.Task;
import com.raheen.taskapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable);
    
    Page<Task> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);
    
    Page<Task> findByUserAndCompleted(User user, boolean completed, Pageable pageable);
    
    Page<Task> findByUserAndTitleContainingIgnoreCaseAndCompleted(User user, String title, boolean completed, Pageable pageable);

    Optional<Task> findByIdAndUser(Long id, User user);
}