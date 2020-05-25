package com.example.demo.app.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Camera {

    public static enum Status {
        NORMAL, DISCONNECTED, ERROR
    }

    @Id
    private Integer id;

    @JsonProperty
    public String getIp() {
        int ip = this.id;
        int mask = 0xFF;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++, ip >>= 8) {
            if (i != 0)
                sb.insert(0, ".");
            sb.insert(0, ip & mask);
        }
        return sb.toString();
    }

    @Enumerated(EnumType.STRING)
    private Status status;
    private String label;

}