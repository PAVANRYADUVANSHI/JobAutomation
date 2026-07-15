package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "application")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "job_listing_id")
    private JobListing jobListing;
    @ManyToOne @JoinColumn(name = "candidate_id")
    private CandidateProfile candidate;
    private String resumeVersion;
    @Column(columnDefinition = "TEXT") private String coverLetter;
    @Builder.Default private String status = "SHORTLISTED"; // SHORTLISTED|APPLIED|RESPONSE|INTERVIEW|REJECTED|OFFER
    private String track;
    private Double matchScore;
    private LocalDateTime submittedAt;
    private LocalDateTime responseAt;
    private LocalDateTime interviewAt;
    private String notes;
    @Builder.Default private Boolean autoSubmitted = false;
    @Builder.Default private Boolean followUpSent = false;
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
