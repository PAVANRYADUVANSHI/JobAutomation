package com.jobautomation.app.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data @Builder
public class AnalyticsResponse {
    private long totalApplications;
    private long shortlisted;
    private long applied;
    private long responses;
    private long interviews;
    private long rejected;
    private long offers;
    private long javaTrackTotal;
    private long genaiTrackTotal;
    private long javaTrackApplied;
    private long genaiTrackApplied;
    private Map<String, Long> dailyCounts;  // date -> count
}
