package com.example.demo.app.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.app.entity.Camera;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CameraRepository extends JpaRepository<Camera, Integer> {
    @Override
    @EntityGraph(value = "camera-user")
    Optional<Camera> findById(Integer id);

    @EntityGraph(value = "camera-task")
    List<Camera> findAllByOrderByIdAsc();

    @EntityGraph(value = "camera-user")
    @Query(value = "SELECT c FROM Camera c")
    List<Camera> findAllWithUsers();
}