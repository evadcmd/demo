package com.example.demo.auth.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Auth implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    private String authority;
    private String displayname;
    private Integer weight;

    @OneToMany(mappedBy = "auth")
    private Set<User> users;

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }

    @Override
    public boolean equals(Object auth) {
        return this.authority.equals(((Auth) auth).getAuthority());
    }
}