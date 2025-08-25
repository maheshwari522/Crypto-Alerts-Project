package com.example.userservice.user;


import com.example.userservice.auth.dto.UpdateUserRequestDTO;
import com.example.userservice.auth.dto.UserResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Page<UserResponseDTO> list(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(u -> new UserResponseDTO(u.getId(), u.getEmail(), u.getFullName(), u.getRole(), u.isEnabled(), u.getLastLoginAt(), u.getCreatedAt(), u.getUpdatedAt()));
    }


    public UserResponseDTO get(UUID id) {
        User u = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponseDTO(u.getId(), u.getEmail(), u.getFullName(), u.getRole(), u.isEnabled(), u.getLastLoginAt(), u.getCreatedAt(), u.getUpdatedAt());
    }


    @Transactional
    public UserResponseDTO update(UUID id, UpdateUserRequestDTO req) {
        User u = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (req.fullName() != null) u.setFullName(req.fullName());
        if (req.enabled() != null) u.setEnabled(req.enabled());
        if (req.newPassword() != null) u.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        return new UserResponseDTO(u.getId(), u.getEmail(), u.getFullName(), u.getRole(), u.isEnabled(), u.getLastLoginAt(), u.getCreatedAt(), u.getUpdatedAt());
    }


    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) throw new IllegalArgumentException("User not found");
        userRepository.deleteById(id);
    }
}