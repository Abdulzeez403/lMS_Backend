package com.lms.lms.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "refresh_tokens")
public class RefreshToken {
    @Id
    private String id;
    private String userId;       // foreign key to User
    private String token;        // the actual token
    private Instant expiryDate;  // optional expiration
    private String deviceInfo;   // optional, track device/browser
}
