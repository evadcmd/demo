package com.example.demo.auth.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

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

    @Id
    private String authority;

    @ManyToMany
    @JoinTable(name = "user_auth",
        joinColumns = @JoinColumn(name = "authority"),
        inverseJoinColumns = @JoinColumn(name = "username")
    )
    private Set<User> users;

    public Auth(String authority) {
        this.authority = authority;
    }
}