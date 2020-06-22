package com.example.demo.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import com.example.demo.Record;
import com.example.demo.app.dto.HumanDetectionResp;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetectionLog extends Record {

    public static final DetectionLog of(HumanDetectionResp resp) {
        return DetectionLog.builder()
            .cameraId(resp.getCameraId())
            .at(new Date())
            .isHuman(resp.getIsHuman())
            .numHuman(resp.getNumHuman())
            .maxConfidence(resp.getMaxConfidence())
            .img(DatatypeConverter.parseBase64Binary(resp.getImg()))
            .build();
    }

    public static final DetectionLog EMTPY = DetectionLog.builder()
            .at(null)
            .cameraId(null)
            .isHuman(false)
            .numHuman(0)
            .img(null)
            .maxConfidence(0)
            .build();

    @Id @GeneratedValue
    private Integer id;
    private Date at;
    private Integer cameraId;
    private Boolean isHuman;
    private Integer numHuman;
    private Integer maxConfidence;

    @Transient
    private String cameraLabel;
    public DetectionLog setCameraLabel(String cameraLabel) {
        this.cameraLabel = cameraLabel;
        return this;
    }

    @Transient
    @Lob
    @Column(columnDefinition = "bytea")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] img;

    // constructor for partial selection
    public DetectionLog(
        Integer id,
        Date at,
        Integer cameraId,
        Boolean isHuman,
        Integer numHuman,
        Integer maxConfidence,
        Date created,
        Date updated
    ) {
        this.id = id;
        this.at = at;
        this.cameraId = cameraId;
        this.isHuman = isHuman;
        this.numHuman = numHuman;
        this.maxConfidence = maxConfidence;
        this.created = created;
        this.updated = updated;
    }

}