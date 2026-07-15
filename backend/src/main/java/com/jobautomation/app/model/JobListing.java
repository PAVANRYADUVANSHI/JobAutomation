package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity @Table(name = "job_listing")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class JobListing {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    private String source;
    private String track;          // JAVA_FULLSTACK | GENAI
    private String title;
    private String company;
    private String location;
    @Column(columnDefinition = "TEXT") private String description;
    private String applyUrl;
    private LocalDate postedDate;
    private String experienceRequired;
    @Builder.Default private Boolean isRemote = false;
    @Builder.Default private Boolean isManualWatchlist = false;
    @Column(unique = true) private String dedupHash;
    @Builder.Default private Double javaMatchScore = 0.0;
    @Builder.Default private Double genaiMatchScore = 0.0;
    private String selectedResumeVersion;
    @Builder.Default private String status = "NEW";
    @Builder.Default private LocalDateTime fetchedAt = LocalDateTime.now();
}
