package com.example.demo.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrincipal {
    private String username;
    private String password;
}