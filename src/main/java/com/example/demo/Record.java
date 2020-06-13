package com.example.demo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * superclass of recordable entity:
 * record created timestamp and updated timestamp automatically.
 * 
 * eg.
 * public class User extends Record implements serializable {
 *      ...
 * }
 */
@MappedSuperclass
public class Record {
    // JsonProperty: necessary when subclass serialization
    @JsonProperty
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    protected Date created;

    @JsonProperty
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    protected Date updated;

    @PrePersist
    protected void onCreate() {
        updated = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}