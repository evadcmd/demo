package com.example.demo.auth.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;

import com.example.demo.app.entity.Camera;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity @Table(name = "demo_user")
@NamedEntityGraphs({
    @NamedEntityGraph(name = "user-camera",
        attributeNodes = {
            @NamedAttributeNode(value = "auth"),
            @NamedAttributeNode(value = "cameras")
        }
    )
})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    public static User empty() {
        return User.builder()
            .username("")
            .email("")
            .password("")
            .build();
    }

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String email;
    private String username;

    @JsonIgnore
    private String password;
    @JsonIgnore
    public String getPassword() {
        return this.password;
    }
    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;

    @ManyToOne
    @JoinColumn(name = "auth_id")
    Auth auth;

    public Set<Auth> getAuthorities() {
        return Set.of(this.auth);
    }

    @ManyToMany
    @JoinTable(name = "user_camera",
        joinColumns = @JoinColumn(name = "id"),
        inverseJoinColumns = @JoinColumn(name = "ip"))
    private Set<Camera> cameras;
}