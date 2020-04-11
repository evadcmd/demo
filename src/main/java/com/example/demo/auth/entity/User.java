package com.example.demo.auth.entity;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "demo_user")
public class User implements UserDetails {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    private String username;
    private String password;
    private String displayName;
    private boolean enabled;
    private boolean credentialsNonExpired;
    private boolean accountNonExpired;
    private boolean accountNonLocked;

    @ManyToMany
    @JoinTable(name = "user_auth",
        joinColumns = @JoinColumn(name="username"),
        inverseJoinColumns = @JoinColumn(name = "authority")
    )
    private Set<Auth> authorities;

}