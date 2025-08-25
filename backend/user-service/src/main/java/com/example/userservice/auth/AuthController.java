package com.example.userservice.auth;




import com.example.userservice.auth.dto.*;
import com.example.userservice.user.User;
import com.example.userservice.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;


    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) {
        return ResponseEntity.ok(authService.register(req));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        return ResponseEntity.ok(authService.login(req));
    }


    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO req) {
        return ResponseEntity.ok(authService.refresh(req));
    }


    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(@AuthenticationPrincipal UserDetails principal) {
        User u = userRepository.findByEmail(principal.getUsername()).orElseThrow();
        return ResponseEntity.ok(new UserResponseDTO(u.getId(), u.getEmail(), u.getFullName(), u.getRole(), u.isEnabled(), u.getLastLoginAt(), u.getCreatedAt(), u.getUpdatedAt()));
    }
}