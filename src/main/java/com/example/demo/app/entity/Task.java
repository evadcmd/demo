package com.example.demo.app.entity;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.Transient;

import com.example.demo.app.dto.HHMM;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(name = "task-camera",
        attributeNodes = {@NamedAttributeNode("cameras")}
    ),
    @NamedEntityGraph(name = "task-camera-user",
        attributeNodes = {@NamedAttributeNode(value = "cameras", subgraph = "camera-user")},
        subgraphs = {
            @NamedSubgraph(name = "camera-user",
                attributeNodes = {@NamedAttributeNode("users")}
            )
        }
    ),
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task {

    public static final LocalDate BASIS = LocalDate.of(1970, Month.of(1), 1); // time basis
    public static final ZoneId ZONE = ZoneId.systemDefault(); // timezone

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
    private Integer duration; // minutes

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

    @JsonSetter
    public void setIndices(List<Integer> indices) {
        this.indices = new byte[50];
        int offset = indices.get(0);
        if ((offset >> 5) > 0) {
            indices.set(0, 0);
            for (int i = 4; i > 0; i--, offset >>= 8)
                this.indices[this.indices.length - i] = (byte) offset;
        }
        int mask = 0b1;
        for (Integer index : indices)
            this.indices[index >> 3] |= (mask << (index & 0b111));
    }

    public HHMM getStart() {
        return HHMM.of(this.start);
    }

    public void setStart(HHMM hhmm) {
        this.start = hhmm.value();
    }

    public HHMM getEnd() {
        return HHMM.of(this.start + this.duration);
    }

    @JsonIgnore
    @Transient
    String[] initCronTokens() {
        String[] tokens = new String[6];
        HHMM hhmm = getStart();
        tokens[0] = String.valueOf(0);
        tokens[1] = String.valueOf(hhmm.getMinute());
        tokens[2] = String.valueOf(hhmm.getHour());
        return tokens;
    }

    private static final String cronString(String[] tokens) {
        return Arrays.stream(tokens).collect(Collectors.joining(" "));
    }

    @JsonIgnore
    @Transient
    public String getCron() {
        if (this.mode != Mode.MONTH && this.mode != Mode.WEEK)
            throw new RuntimeException();

        String[] tokens = null;
        switch (this.mode) {
            case WEEK:
                tokens = initCronTokens();
                tokens[3] = "?";
                tokens[4] = "*";
                tokens[5] = getIndices().stream().map(String::valueOf).collect(Collectors.joining(","));
                return cronString(tokens);
            case MONTH:
                tokens = initCronTokens();
                tokens[3] = getIndices().stream().map(String::valueOf).collect(Collectors.joining(","));
                tokens[4] = "*"; 
                tokens[5] = "?";
                return cronString(tokens);
            default:
                return "";
        }
    }

    @JsonIgnore
    @Transient
    public List<Date> getTriggerDates() {
        if (this.mode != Mode.SINGLE_SHOT)
            throw new RuntimeException();

        List<Date> dates = new LinkedList<>();
        int offset = 0;
        for (int i = 1; i <= 4; i++) {
            offset <<= 8;
            offset += (this.indices[this.indices.length - i] & 0xFF);
        }

        ZonedDateTime startAt = BASIS.plusDays(offset).atStartOfDay(ZONE);
        startAt = startAt.plusMinutes(this.start);
        dates.add(Date.from(startAt.toInstant()));
        for (int i = 1, mask = 0b1; i < this.mode.limit; i++)
            if ((this.indices[i >> 3] & mask << (i & 0b111)) != 0)
                dates.add(Date.from(startAt.plusDays(i).toInstant()));
        return dates;
    }

    @ManyToMany
    @JoinTable(name = "camera_task",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "ip"))
    Set<Camera> cameras;

}