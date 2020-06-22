package com.example.demo.auth.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.auth.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    @EntityGraph(value = "user-camera")
    Optional<User> findByEmail(String email);

    @EntityGraph(value = "user-camera")
    @Query("SELECT u FROM User u LEFT JOIN u.auth auth WHERE auth.authority != 'ROLE_ROOT'")
    List<User> findAllWithoutRoot();
}