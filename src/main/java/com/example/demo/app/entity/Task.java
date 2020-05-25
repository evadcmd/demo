package com.example.demo.app.entity;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Task {
    // time basis
    public static final LocalDate BASIS = LocalDate.of(1970, Month.of(1), 1);
    // timezone
    public static final ZoneId ZONE = ZoneId.systemDefault();

    public static enum Mode {
        WEEK("毎週", 7),
        MONTH("毎月", 31),
        SINGLE_SHOT("一回", 365);

        @Getter private String label;
        @Getter private int limit;

        private Mode(String label, int limit) {
            this.label = label;
            this.limit = limit;
        }
    }

    @Id @GeneratedValue
    private Integer id;

    private String label;

    // cron related
    @Enumerated(EnumType.STRING)
    private Mode mode = Mode.WEEK;

    /**
     * day offset from BASIS(1970/01/01): 4 bytes
     * indices: 46 bytes (0-365)
     */
    @Lob
    @Column(columnDefinition = "bytea")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] indices;

    private Integer start;
    private Integer duration;

    public List<Integer> getIndices() {
        if (this.indices == null)
            return new ArrayList<Integer>(0);

        final int limit = this.mode.getLimit();
        int mask = 0b1;
        List<Integer> res = new ArrayList<Integer>(limit);
        switch (this.mode) {
            case SINGLE_SHOT:
                int diff = 0;
                for (int i = 1; i < 4; i++) {
                    diff <<= 8;
                    diff |= (0xFF & this.indices[this.indices.length - i]);
                }
                res.add(diff);
            case WEEK:
            case MONTH:
                for (int i = 1; i < limit; i++) {
                    if (((mask << (i & 0b111)) & indices[i >> 3]) != 0)
                        res.add(i);
                }
        }
        return res;
    }

}