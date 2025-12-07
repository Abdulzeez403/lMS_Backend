package com.lms.lms.controller;

import com.lms.lms.model.User;
import com.lms.lms.repositories.UserRepository;
import com.lms.lms.service.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

  

 @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {
    // 1. Check if username already exists
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        return ResponseEntity
                .badRequest()
                .body("Username already exists!");
    }

    // 2. Hash the password
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // 3. Save user to DB
    userRepository.save(user);

    // 4. Return success response
    return ResponseEntity.ok("User registered successfully!");
}

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(dbUser.getUsername());
    }
}
