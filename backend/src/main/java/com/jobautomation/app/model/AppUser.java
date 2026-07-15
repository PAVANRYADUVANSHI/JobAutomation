package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "app_user")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true) private String username;
    private String password;
    @Builder.Default private String role = "ROLE_USER";
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
