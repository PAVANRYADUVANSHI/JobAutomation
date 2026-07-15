package com.jobautomation.app.controller;

import com.jobautomation.app.model.JobListing;
import com.jobautomation.app.repository.JobListingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/jobs")
@RequiredArgsConstructor @Tag(name = "Jobs")
public class JobController {

    private final JobListingRepository jobListingRepo;

    @GetMapping
    @Operation(summary = "List all job listings with optional status/track filter")
    public ResponseEntity<Page<JobListing>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String track) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("fetchedAt").descending());
        if (status != null && track != null) {
            return ResponseEntity.ok(new PageImpl<>(
                jobListingRepo.findByStatusAndTrack(status, track), pageable,
                jobListingRepo.findByStatusAndTrack(status, track).size()));
        }
        if (status != null) return ResponseEntity.ok(jobListingRepo.findByStatus(status, pageable));
        return ResponseEntity.ok(jobListingRepo.findAll(pageable));
    }

    @GetMapping("/shortlisted")
    @Operation(summary = "Get today's top 25 shortlisted jobs ordered by match score")
    public ResponseEntity<List<JobListing>> shortlisted() {
        return ResponseEntity.ok(jobListingRepo.findShortlistedOrderByScore(PageRequest.of(0, 50)));
    }

    @GetMapping("/watchlist")
    @Operation(summary = "Get manual watchlist entries (companies with no API)")
    public ResponseEntity<List<JobListing>> watchlist() {
        return ResponseEntity.ok(jobListingRepo.findByStatusAndTrack("MANUAL_WATCHLIST", null)
            .isEmpty() ? jobListingRepo.findAll().stream()
                .filter(j -> Boolean.TRUE.equals(j.getIsManualWatchlist())).toList()
            : jobListingRepo.findAll().stream()
                .filter(j -> Boolean.TRUE.equals(j.getIsManualWatchlist())).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobListing> get(@PathVariable Long id) {
        return jobListingRepo.findById(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
