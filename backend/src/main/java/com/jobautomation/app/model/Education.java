package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "education")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Education {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "candidate_id")
    private CandidateProfile candidate;
    private String degree;
    private String certification;
    private String institution;
    private String years;
    private String percentage;
    @Column(columnDefinition = "JSON")
    private String coursework;
}
