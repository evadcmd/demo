package com.example.demo.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class HumanDetectionResp {

    public static final HumanDetectionResp empty() {
        return HumanDetectionResp.builder().status(500).build();
    }

    private Integer status;
    private Integer cameraId;

    @JsonProperty(required = false)
    private String msg;
    @JsonProperty(required = false)
    private String img;
    @JsonProperty(required = false)
    private Integer numHuman;
    @JsonProperty(required = false)
    private Boolean isHuman;
    @JsonProperty(required = false)
    private Integer maxConfidence;
}