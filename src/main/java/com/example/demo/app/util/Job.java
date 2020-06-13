package com.example.demo.app.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.demo.app.entity.Camera;
import com.example.demo.app.entity.Task;
import com.example.demo.app.repository.TaskRepository;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Job implements org.quartz.InterruptableJob {
    private static final ZoneId ZONE = ZoneId.systemDefault();

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CameraBag cameraBag;

    @Setter
    private Integer id; // task ID

    // reminder for interrupt job
    private List<Camera> cameras;

    public Job() {
        this.cameras = new ArrayList<>();
    }

    private void removeCamerasFromBag() {
        for (Camera camera : this.cameras)
            cameraBag.remove(camera);
        log.info("remove task{}", this.id);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Task task = taskRepository.findById(id).orElse(null);
        // task has been deleted
        if (task == null)
            return;

        LocalDateTime fireTime = LocalDateTime.ofInstant(
            context.getFireTime().toInstant(), ZONE);

        LocalDateTime startTime = LocalDateTime.ofInstant(
            context.getTrigger().getStartTime().toInstant(), ZONE);

        long duration = ChronoUnit.MILLIS.between(
            fireTime,
            startTime.plusMinutes(task.getDuration())
        );

        if (duration < 0)
            return;

        try {
            for (Camera camera : task.getCameras()) {
                cameras.add(camera);
                cameraBag.put(camera);
            }
            log.info("run task{}", this.id);
            TimeUnit.MILLISECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            removeCamerasFromBag();
        }

    }

    @Override
    public void interrupt() {
        removeCamerasFromBag();
    }

}