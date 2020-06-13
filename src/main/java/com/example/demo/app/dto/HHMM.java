package com.example.demo.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HHMM {
    public static final HHMM of(int epoch) {
        return new HHMM(epoch);
    }

    private int hour;
    private int minute;

    private HHMM(int epoch) {
        this.hour = (epoch / 60) % 24;
        this.minute = epoch % 60;
    }

    public int value() {
        return hour * 60 + minute;
    }

}