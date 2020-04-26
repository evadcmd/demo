package com.example.demo.auth.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "demo_user")
public class User implements UserDetails {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String email;
    private String username;
    private String password;
    private boolean enabled = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_auth",
        joinColumns = @JoinColumn(name="username"),
        inverseJoinColumns = @JoinColumn(name = "authority")
    )
    private Set<Auth> authorities;

}