package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "resume_project")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeProject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "resume_version_id")
    private ResumeVersion resumeVersion;
    private String name;
    private String type;
    private String dates;
    private String githubUrl;
    @Column(columnDefinition = "JSON") private String stack;
    @Column(columnDefinition = "TEXT") private String highlight;
    @Column(columnDefinition = "TEXT") private String coverLetterPitch;
}
