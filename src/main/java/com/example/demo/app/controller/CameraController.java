package com.example.demo.app.controller;

import java.util.List;

import com.example.demo.app.entity.Camera;
import com.example.demo.app.repository.CameraRepository;
import com.example.demo.app.repository.TaskRepository;
import com.example.demo.app.util.Scheduler;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping(CameraController.URL)
public class CameraController {
    public static final String URL = "/api/settings";

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private CameraRepository cameraRepository;

    /**
     * Modifying camera entity is not going to delete any task, so it is safe to
     * just update all tasks when camera has been modified.
     */
    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/camera")
    @GetMapping
    List<Camera> list() {
        return cameraRepository.findAll();
    }

    @GetMapping
    List<Camera> get() {
        return cameraRepository.findAllByOrderByIdAsc();
    }

    @PreAuthorize("hasRole('ROOT') OR hasRole('ADMIN')")
    @PostMapping
    List<Camera> post(@RequestBody Camera camera) throws SchedulerException {
        cameraRepository.save(camera);
        scheduler.update(taskRepository.findAll());
        return cameraRepository.findAllByOrderByIdAsc();
    }

    @PreAuthorize("hasRole('ROOT') OR hasRole('ADMIN')")
    @DeleteMapping
    List<Camera> delete(@RequestParam Integer id) throws SchedulerException {
        cameraRepository.deleteById(id);
        scheduler.update(taskRepository.findAll());
        return cameraRepository.findAllByOrderByIdAsc();
    } 
}