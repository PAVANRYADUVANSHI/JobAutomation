package com.jobautomation.app.controller;

import com.jobautomation.app.dto.AnalyticsResponse;
import com.jobautomation.app.model.SchedulerLog;
import com.jobautomation.app.repository.SchedulerLogRepository;
import com.jobautomation.app.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/analytics")
@RequiredArgsConstructor @Tag(name = "Analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final SchedulerLogRepository schedulerLogRepo;

    @GetMapping
    @Operation(summary = "Get full analytics: pipeline counts, track split, daily chart data")
    public ResponseEntity<AnalyticsResponse> analytics() {
        return ResponseEntity.ok(analyticsService.getAnalytics());
    }

    @GetMapping("/scheduler-logs")
    @Operation(summary = "Get last 10 scheduler run logs")
    public ResponseEntity<List<SchedulerLog>> schedulerLogs() {
        return ResponseEntity.ok(schedulerLogRepo.findTop10ByOrderByRunAtDesc());
    }
}
