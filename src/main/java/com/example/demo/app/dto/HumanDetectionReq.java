package com.example.demo.app.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
// @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HumanDetectionReq {
    public static final HumanDetectionReq of(Integer cameraId, float threshold) {
        return new HumanDetectionReq(cameraId, threshold);
    }

    private Integer cameraId;
    private float threshode;
}