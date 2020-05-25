package com.example.demo.auth.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Auth implements GrantedAuthority {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static Auth of(String role) {
        return new Auth(role);
    }

    @Id
    private String authority;

    public String getAuthority() {
        return this.authority.toString();
    }

    @JsonIgnore
    @Setter
    @Getter
    @ManyToMany
    @JoinTable(name = "user_auth",
        joinColumns = @JoinColumn(name = "authority"),
        inverseJoinColumns = @JoinColumn(name = "username")
    )
    private Set<User> users;

    private Auth(String role) {
        this.authority = role;
    }

}