package com.example.demo.app.repository;

import java.util.Date;
import java.util.List;

import com.example.demo.app.entity.DetectionLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DetectionLogRepository extends JpaRepository<DetectionLog, Integer> {
    @Query(value = "SELECT new DetectionLog(l.id, l.at, l.cameraId, l.isHuman, l.numHuman, l.maxConfidence, l.created, l.updated) FROM DetectionLog l WHERE l.at > ?1 AND l.at < ?2 ORDER BY l.at")
    List<DetectionLog> findAllWithoutImage(Date from, Date to);
}