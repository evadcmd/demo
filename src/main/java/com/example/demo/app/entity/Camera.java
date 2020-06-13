package com.example.demo.app.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;

import com.example.demo.app.util.IP;
import com.example.demo.auth.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(name = "camera-task", attributeNodes = {@NamedAttributeNode("tasks")}),
    @NamedEntityGraph(name = "camera-user", attributeNodes = {@NamedAttributeNode("users")}),
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Camera {

    public static enum Status {
        NORMAL, DISCONNECTED, ERROR
    }

    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String label;

    @ManyToMany
    @JoinTable(name = "camera_task",
        joinColumns = @JoinColumn(name = "ip"),
        inverseJoinColumns = @JoinColumn(name = "id"))
    Set<Task> tasks;

    @ManyToMany
    @JoinTable(name = "user_camera",
        joinColumns = @JoinColumn(name = "ip"),
        inverseJoinColumns = @JoinColumn(name = "id"))
    Set<User> users;

    @JsonProperty
    public String getIp() {
        return IP.valueOf(this.id);
    }

    /*
    @JsonProperty
    public Integer getValue() {
        return this.id;
    }
    */
}