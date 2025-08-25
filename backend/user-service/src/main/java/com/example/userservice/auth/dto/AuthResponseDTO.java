package com.example.userservice.auth.dto;


public record AuthResponseDTO(
        String tokenType,
        String accessToken,
        long accessTokenExpiresInSeconds,
        String refreshToken,
        long refreshTokenExpiresInSeconds
) {
    public static AuthResponseDTO bearer(String access, long accessTtlSeconds, String refresh, long refreshTtlSeconds) {
        return new AuthResponseDTO("Bearer", access, accessTtlSeconds, refresh, refreshTtlSeconds);
    }
}