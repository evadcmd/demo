package com.example.demo.auth.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
public class Auth implements GrantedAuthority {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static enum Role {
        ROLE_ADMIN, ROLE_MANAGER, ROLE_USER;
    }

    public static Auth of(Role role) {
        return new Auth(role);
    }

    @Id
    @Enumerated(EnumType.STRING)
    @Setter
    private Role authority;

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

    private Auth(Role role) {
        this.authority = role;
    }

}