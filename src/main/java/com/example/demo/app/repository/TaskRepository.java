package com.example.demo.app.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.app.entity.Task;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Override
    @EntityGraph(value = "task-camera-user")
    Optional<Task> findById(Integer id);

    @EntityGraph(value = "task-camera")
    List<Task> findAllByOrderByIdAsc();
}