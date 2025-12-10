package com.lms.lms.controller;

import com.lms.lms.dto.UserRequestDTO;
import com.lms.lms.dto.UserResponseDTO;
import com.lms.lms.model.RefreshToken;
import com.lms.lms.model.User;
import com.lms.lms.repositories.UserRepository;
import com.lms.lms.service.JwtUtil;
import com.lms.lms.service.RefreshTokenService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

  

@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody UserRequestDTO request) {
    // 1️⃣ Check if username already exists
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
        throw new RuntimeException("Username already exists!");
    }

    // 2️⃣ Map DTO -> entity
    User user = new User();
    user.setUsername(request.getUsername());
    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole("USER"); // default role

    // 3️⃣ Save to DB
    userRepository.save(user);

    // 4️⃣ Map entity -> response DTO (optional)
    UserResponseDTO response = new UserResponseDTO(
        user.getId(),
        user.getUsername(),
        user.getName(),
        user.getEmail(),
        user.getRole()
    );

    return ResponseEntity.ok(response);
}


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        User dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken =jwtUtil.generateToken(dbUser.getUsername(), dbUser.getRole());

          RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

    return ResponseEntity.ok(Map.of(
        "accessToken", accessToken,
        "refreshToken", refreshToken.getToken()
    ));



    }

    @PostMapping("/refresh-token")
public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
    String requestToken = request.get("refreshToken");

    RefreshToken refreshToken = refreshTokenService.findByToken(requestToken);


    if (refreshTokenService.isExpired(refreshToken)) {
        refreshTokenService.deleteByUserId(refreshToken.getUserId());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Refresh token expired, please login again");
    }

     // 3. Get user details
    User dbUser = userRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

    String newAccessToken = jwtUtil.generateToken(refreshToken.getUserId(), dbUser.getRole());

    return ResponseEntity.ok(Map.of(
        "accessToken", newAccessToken,
        "refreshToken", refreshToken.getToken() // can rotate if needed
    ));
}

@PostMapping("/logout")
public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
    String userId = request.get("userId");
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok("Logged out successfully");
}

}


