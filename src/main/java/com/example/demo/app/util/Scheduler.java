package com.example.demo.app.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.app.entity.Task;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Scheduler {

    @Autowired
    private org.quartz.Scheduler scheduler;

    private void addCronTriggerTask(Task task) throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(String.valueOf(0), task.getId().toString())
            .withSchedule(CronScheduleBuilder.cronSchedule(task.getCron()))
            .build();
        JobDetail jobDetail = JobBuilder.newJob(Job.class)
            .withIdentity(String.valueOf(0), task.getId().toString())
            .usingJobData("id", task.getId())
            .build();
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private void addSimpleTriggerTask(Task task) throws SchedulerException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        for (Date date : task.getTriggerDates()) {
            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(fmt.format(date), task.getId().toString())
                .startAt(date)
                .build();
            JobDetail jobDetail = JobBuilder.newJob(Job.class)
                .withIdentity(fmt.format(date), task.getId().toString())
                .usingJobData("id", task.getId())
                .build();
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    public void activateDaemonProcess() {

    }

    public void add(Task task) throws SchedulerException {
        switch (task.getMode()) {
            case WEEK:
            case MONTH:
                addCronTriggerTask(task);
                break;
            case SINGLE_SHOT:
                addSimpleTriggerTask(task);
                break;
        }
        log.info("add task{}", task.getId());
    }

    public void delete(Task task) throws SchedulerException {
        List<JobKey> jobKeys = scheduler
                                .getJobKeys(GroupMatcher.jobGroupEquals(task.getId().toString()))
                                .stream()
                                .collect(Collectors.toList());
        scheduler.getCurrentlyExecutingJobs();
        for (JobKey jobKey : jobKeys)
            scheduler.interrupt(jobKey);
        scheduler.deleteJobs(jobKeys);
    }

    public void unschedule(Task task) throws SchedulerException {
        scheduler.unscheduleJobs(
            scheduler
                .getTriggerKeys(GroupMatcher.triggerGroupEquals(task.getId().toString()))
                .stream()
                .collect(Collectors.toList())
        );
    }

    public void add(Collection<Task> tasks) throws SchedulerException {
        for (Task task : tasks)
            add(task);
    }

    public void update(Task task) throws SchedulerException {
        delete(task);
        add(task);
    }

}