package com.example.userservice.auth.dto;


import com.example.userservice.user.Role;


import java.time.Instant;
import java.util.UUID;


public record UserResponseDTO(
        UUID id,
        String email,
        String fullName,
        Role role,
        boolean enabled,
        Instant lastLoginAt,
        Instant createdAt,
        Instant updatedAt
) {}