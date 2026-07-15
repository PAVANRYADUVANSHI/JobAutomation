package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "scheduler_log")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SchedulerLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Builder.Default private LocalDateTime runAt = LocalDateTime.now();
    @Builder.Default private Integer jobsFetched = 0;
    @Builder.Default private Integer jobsMatched = 0;
    @Builder.Default private Integer jobsShortlisted = 0;
    @Builder.Default private Integer jobsSubmitted = 0;
    private String mode;
    @Column(columnDefinition = "TEXT") private String summary;
}
