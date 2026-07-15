package com.jobautomation.app.service;

import com.jobautomation.app.dto.AnalyticsResponse;
import com.jobautomation.app.repository.ApplicationRepository;
import com.jobautomation.app.repository.JobListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service @RequiredArgsConstructor
public class AnalyticsService {

    private final ApplicationRepository applicationRepo;
    private final JobListingRepository jobListingRepo;

    public AnalyticsResponse getAnalytics() {
        Map<String, Long> daily = new LinkedHashMap<>();
        for (Object[] row : applicationRepo.dailyApplicationCounts()) {
            daily.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }

        return AnalyticsResponse.builder()
            .totalApplications(applicationRepo.count())
            .shortlisted(applicationRepo.countByStatus("SHORTLISTED"))
            .applied(applicationRepo.countByStatus("APPLIED"))
            .responses(applicationRepo.countByStatus("RESPONSE"))
            .interviews(applicationRepo.countByStatus("INTERVIEW"))
            .rejected(applicationRepo.countByStatus("REJECTED"))
            .offers(applicationRepo.countByStatus("OFFER"))
            .javaTrackTotal(applicationRepo.countByTrack("JAVA_FULLSTACK"))
            .genaiTrackTotal(applicationRepo.countByTrack("GENAI"))
            .javaTrackApplied(applicationRepo.countByTrackAndStatus("JAVA_FULLSTACK", "APPLIED"))
            .genaiTrackApplied(applicationRepo.countByTrackAndStatus("GENAI", "APPLIED"))
            .dailyCounts(daily)
            .build();
    }
}
