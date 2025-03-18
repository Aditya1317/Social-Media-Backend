package com.task1.Task.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generateToken(String username,Long userId);

    Long extractUserId(String token);

    String extractUserName(String token);

    boolean validateToken(String token, UserDetails userDetails);
}
