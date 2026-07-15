package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "resume_version")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeVersion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "candidate_id")
    private CandidateProfile candidate;
    private String versionKey;   // javaFullStack | genAILeaning
    @Column(columnDefinition = "JSON") private String targetRoles;
    @Column(columnDefinition = "TEXT") private String summary;
    @Column(columnDefinition = "JSON") private String coreSkills;
    @Column(columnDefinition = "JSON") private String keywordWeights;
    private String resumeFilePath;
}
