package com.example.demo.app.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.example.demo.app.dto.HumanDetectionReq;
import com.example.demo.app.dto.HumanDetectionResp;
import com.example.demo.app.entity.Camera;
import com.example.demo.app.entity.DetectionLog;
import com.example.demo.app.repository.CameraRepository;
import com.example.demo.app.repository.DetectionLogRepository;
import com.example.demo.auth.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DaemonProcessTask implements org.quartz.Job {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private URL url;
    @Autowired private CameraBag bag;

    @Autowired private Mail mail;
    @Autowired private CameraRepository cameraRepository;
    @Autowired private DetectionLogRepository logRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<CompletableFuture<HumanDetectionResp>> futures = new LinkedList<>();

        for (Map.Entry<Integer, Integer> entry : bag.entrySet()) {
            if (entry.getValue() == 0)
                continue;

            Integer cameraId = entry.getKey();
            String ip = IP.valueOf(cameraId);
            log.info("Daemon Process:{}", ip);

            String json = "";
            try {
                json = objectMapper.writeValueAsString(HumanDetectionReq.of(0, 0.4F));
            } catch (JsonProcessingException e) {
                return;
            }

            // build a request
            HttpRequest request = HttpRequest.newBuilder(URI.create(url.toString()))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            // send a request
            futures.add(
                HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(body -> {
                        try {
                            HumanDetectionResp resp = objectMapper.readValue(body, HumanDetectionResp.class);
                            resp.setCameraId(cameraId);
                            return resp;
                        } catch (IOException e) {
                            return HumanDetectionResp.empty();
                        }
                    })
                    .exceptionally(e -> {
                      return HumanDetectionResp.empty();  
                    })
            );

            // join all futures
            CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[futures.size()])
            ).join();

            // send mail and save responses as logs
            List<DetectionLog> logs = new ArrayList<>();
            for (CompletableFuture<HumanDetectionResp> future : futures) {
                try {
                    DetectionLog log = DetectionLog.of(future.get());
                    if (log.getIsHuman()) {
                        Camera camera = cameraRepository.findById(log.getCameraId()).orElseThrow();
                        logs.add(log.setCameraLabel(camera.getLabel()));
                        // send mails
                        for (User user : camera.getUsers())
                            mail.send(user, log);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // save logs
            logRepository.saveAll(logs);
        }
    }
}