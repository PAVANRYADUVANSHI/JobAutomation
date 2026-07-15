package com.jobautomation.app.controller;

import com.jobautomation.app.model.Application;
import com.jobautomation.app.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/scheduler")
@RequiredArgsConstructor @Tag(name = "Scheduler")
public class SchedulerController {

    private final JobAggregatorService aggregatorService;
    private final ApplicationService applicationService;

    @PostMapping("/run")
    @Operation(summary = "Manually trigger the full daily pipeline (fetch → match → shortlist)")
    public ResponseEntity<Map<String, Object>> runPipeline() {
        int fetched = aggregatorService.fetchAll();
        List<com.jobautomation.app.model.Application> apps = applicationService.shortlistToday();
        return ResponseEntity.ok(Map.of(
            "fetched", fetched,
            "shortlisted", apps.size(),
            "message", fetched == 0
                ? "No new jobs found. APIs may be rate-limited or no fresher roles matched today."
                : "Pipeline complete — " + apps.size() + " jobs shortlisted from " + fetched + " fetched"
        ));
    }

    @PostMapping("/fetch")
    @Operation(summary = "Fetch fresh job listings only (no shortlisting)")
    public ResponseEntity<Map<String, Integer>> fetch() {
        return ResponseEntity.ok(Map.of("fetched", aggregatorService.fetchAll()));
    }
}
