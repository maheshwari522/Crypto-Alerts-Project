package com.example.userservice.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.time.Instant;
import java.util.UUID;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Email @NotBlank
    @Column(nullable = false, unique = true)
    private String email;


    @NotBlank
    @Column(nullable = false)
    private String passwordHash;


    @NotBlank
    private String fullName;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;


    @Column(nullable = false)
    private boolean enabled = true;


    private Instant lastLoginAt;


    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;


    @UpdateTimestamp
    private Instant updatedAt;
}