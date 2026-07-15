package com.jobautomation.app.controller;

import com.jobautomation.app.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@RestController @RequestMapping("/api/scheduler")
@RequiredArgsConstructor @Tag(name = "Scheduler")
public class SchedulerController {

    private final JobAggregatorService aggregatorService;
    private final ApplicationService applicationService;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile Map<String, Object> lastResult = null;

    @PostMapping("/run")
    @Operation(summary = "Start pipeline in background; poll /status for result")
    public ResponseEntity<Map<String, Object>> runPipeline() {
        if (running.get()) {
            return ResponseEntity.ok(Map.of("status", "running", "message", "Pipeline already running"));
        }
        running.set(true);
        lastResult = null;
        Thread.ofVirtual().start(() -> {
            try {
                int fetched = aggregatorService.fetchAll();
                List<com.jobautomation.app.model.Application> apps = applicationService.shortlistToday();
                lastResult = Map.of(
                    "status", "done",
                    "fetched", fetched,
                    "shortlisted", apps.size(),
                    "message", fetched == 0
                        ? "No new jobs found. APIs may be rate-limited or no fresher roles matched today."
                        : "Pipeline complete — " + apps.size() + " jobs shortlisted from " + fetched + " fetched"
                );
            } catch (Exception e) {
                lastResult = Map.of("status", "error", "message", "Pipeline failed: " + e.getMessage());
            } finally {
                running.set(false);
            }
        });
        return ResponseEntity.ok(Map.of("status", "running", "message", "Pipeline started"));
    }

    @GetMapping("/status")
    @Operation(summary = "Poll pipeline status")
    public ResponseEntity<Map<String, Object>> status() {
        if (running.get()) return ResponseEntity.ok(Map.of("status", "running"));
        if (lastResult != null) return ResponseEntity.ok(lastResult);
        return ResponseEntity.ok(Map.of("status", "idle"));
    }

    @PostMapping("/fetch")
    @Operation(summary = "Fetch fresh job listings only (no shortlisting)")
    public ResponseEntity<Map<String, Integer>> fetch() {
        return ResponseEntity.ok(Map.of("fetched", aggregatorService.fetchAll()));
    }
}
