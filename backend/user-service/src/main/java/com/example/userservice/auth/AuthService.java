package com.example.userservice.auth;


import com.example.userservice.auth.dto.AuthResponseDTO;
import com.example.userservice.auth.dto.LoginRequestDTO;
import com.example.userservice.auth.dto.RefreshTokenRequestDTO;
import com.example.userservice.auth.dto.RegisterRequestDTO;
import com.example.userservice.security.JwtService;
import com.example.userservice.user.Role;
import com.example.userservice.user.User;
import com.example.userservice.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Map;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public AuthService(UserRepository userRepository, PasswordEncoder encoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered");
        }
        User user = User.builder()
                .email(req.email().toLowerCase())
                .passwordHash(passwordEncoder.encode(req.password()))
                .fullName(req.fullName())
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(user);
        return buildTokens(user.getEmail());
    }


    @Transactional
    public AuthResponseDTO login(LoginRequestDTO req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email().toLowerCase(), req.password())
        );
        User user = userRepository.findByEmail(req.email().toLowerCase()).orElseThrow();
        user.setLastLoginAt(Instant.now());
        return buildTokens(user.getEmail());
    }


    public AuthResponseDTO refresh(RefreshTokenRequestDTO req) {
        String subject = jwtService.extractSubject(req.refreshToken());
        if (!jwtService.isTokenValid(req.refreshToken(), subject)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }
        return buildTokens(subject);
    }


    private AuthResponseDTO buildTokens(String email) {
        String access = jwtService.generateAccessToken(email, Map.of("scope", "user"));
        String refresh = jwtService.generateRefreshToken(email);
        return AuthResponseDTO.bearer(access, jwtService.getAccessMillis()/1000, refresh, jwtService.getRefreshMillis()/1000);
    }
}