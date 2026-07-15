package com.jobautomation.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Table(name = "target_company")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TargetCompany {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;      // STARTUP | MNC | PRODUCT | UNICORN
    private String atsType;       // GREENHOUSE | LEVER | ASHBY | WORKDAY | NONE
    private String careersUrl;
    private String apiSlug;
    private String hrEmail;
    @Builder.Default private Boolean isManualWatchlist = false;
    @Builder.Default private Boolean active = true;
    @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
}
