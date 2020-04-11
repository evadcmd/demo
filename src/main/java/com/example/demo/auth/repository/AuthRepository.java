package com.example.demo.auth.repository;

import com.example.demo.auth.entity.Auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, String> {

}