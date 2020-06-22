package com.example.demo.app.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.app.entity.Task;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Override
    @EntityGraph(value = "task-camera-user")
    Optional<Task> findById(Integer id);

    @EntityGraph(value = "task.camera")
    @Query("SELECT task FROM Task task WHERE task.id = ?1")
    Optional<Task> findByIdWithCameras(Integer id);

    @EntityGraph(value = "task-camera")
    List<Task> findAllByOrderByIdAsc();
}