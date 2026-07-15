package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "candidate_profile")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CandidateProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String linkedin;
    private String githubHandle;
    private String portfolio;
    private String location;
    private String experienceLevel;
    @Builder.Default private String reviewMode = "MANUAL";
    @Builder.Default private Integer dailyShortlistTarget = 50;
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
