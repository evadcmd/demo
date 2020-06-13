package com.example.demo.auth.repository;

import java.util.List;

import com.example.demo.auth.entity.Auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthRepository extends JpaRepository<Auth, String> {
    @Query("SELECT auth FROM Auth auth WHERE auth.authority != 'ROLE_ROOT'")
    public List<Auth> findAllWithoutRoot();
}