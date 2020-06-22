package com.example.demo.app.controller;

import java.util.List;

import com.example.demo.app.entity.Task;
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
@RequestMapping(TaskController.URL)
public class TaskController {
    public static final String URL = "/api/settings/task";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Scheduler scheduler;

    @GetMapping
    List<Task> get() {
        return taskRepository.findAllByOrderByIdAsc();
    }

    @PreAuthorize("hasRole('ROOT') OR hasRole('ADMIN')")
    @PostMapping
    List<Task> post(@RequestBody Task task) throws SchedulerException {
        taskRepository.save(task);
        scheduler.update(task);
        return taskRepository.findAllByOrderByIdAsc();
    }

    @PreAuthorize("hasRole('ROOT') OR hasRole('ADMIN')")
    @DeleteMapping
    List<Task> delete(@RequestParam Integer id) throws SchedulerException {
        // finding methods must be using same entity graph
        Task task = taskRepository.findByIdWithCameras(id).orElseThrow();
        scheduler.delete(task);
        taskRepository.delete(task);
        return taskRepository.findAllByOrderByIdAsc();
    }
}