package com.lms.lms.service;

import com.lms.lms.model.RefreshToken;
import com.lms.lms.repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final long refreshDurationMs = 7 * 24 * 60 * 60 * 1000; // 7 days

    

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken createRefreshToken(String userId) {
        RefreshToken token = new RefreshToken();
        token.setUserId(userId);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshDurationMs));
        return repository.save(token);
    }

    public boolean isExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public void deleteByUserId(String userId) {
        repository.deleteByUserId(userId);
    }

    public RefreshToken findByToken(String token) {
        return repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }
}
