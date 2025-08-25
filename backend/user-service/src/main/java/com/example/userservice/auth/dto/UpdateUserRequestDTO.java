package com.example.userservice.auth.dto;


import jakarta.validation.constraints.Size;


public record UpdateUserRequestDTO(
        String fullName,
        @Size(min = 8, max = 100) String newPassword,
        Boolean enabled
) {}