package com.lms.lms.controller;

import com.lms.lms.model.User;
import com.lms.lms.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    // Get all users (protected by JWT)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
