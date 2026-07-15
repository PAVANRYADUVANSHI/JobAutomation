package com.jobautomation.app.controller;

import com.jobautomation.app.dto.ApplicationStatusUpdate;
import com.jobautomation.app.model.Application;
import com.jobautomation.app.repository.ApplicationRepository;
import com.jobautomation.app.service.ApplicationService;
import com.jobautomation.app.service.CoverLetterService;
import com.jobautomation.app.service.EmailApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController @RequestMapping("/api/applications")
@RequiredArgsConstructor @Tag(name = "Applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepo;
    private final EmailApplyService emailApplyService;
    private final CoverLetterService coverLetterService;

    @GetMapping("/queue")
    @Operation(summary = "Get today's review queue (SHORTLISTED applications)")
    public ResponseEntity<List<Application>> queue() {
        return ResponseEntity.ok(applicationService.getTodayQueue());
    }

    @GetMapping
    @Operation(summary = "List all applications with pagination")
    public ResponseEntity<Page<Application>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        if (status != null) return ResponseEntity.ok(applicationRepo.findByStatus(status, pageable));
        return ResponseEntity.ok(applicationRepo.findAll(pageable));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update application status (approve/reject/move pipeline)")
    public ResponseEntity<Application> updateStatus(
            @PathVariable Long id, @RequestBody ApplicationStatusUpdate update) {
        return ResponseEntity.ok(applicationService.updateStatus(id, update.getStatus(), update.getNotes()));
    }

    @PostMapping("/shortlist")
    @Operation(summary = "Manually trigger shortlisting (score + rank today's jobs)")
    public ResponseEntity<List<Application>> shortlist() {
        return ResponseEntity.ok(applicationService.shortlistToday());
    }

    @PostMapping("/{id}/regenerate-cover-letter")
    @Operation(summary = "Regenerate cover letter for an application using latest template")
    public ResponseEntity<Application> regenerateCoverLetter(@PathVariable Long id) {
        Application app = applicationRepo.findById(id).orElseThrow();
        app.setCoverLetter(coverLetterService.generate(app.getJobListing(), app.getResumeVersion()));
        return ResponseEntity.ok(applicationRepo.save(app));
    }

    @PostMapping("/{id}/email-apply")
    @Operation(summary = "Send application email with resume to company HR for a single job")
    public ResponseEntity<Map<String, Object>> emailApply(@PathVariable Long id) {
        Application app = applicationRepo.findById(id).orElseThrow();
        boolean sent = emailApplyService.applyByEmail(app);
        return ResponseEntity.ok(Map.of(
            "sent", sent,
            "message", sent ? "Email sent to company HR" : "No HR email configured for this company"
        ));
    }

    @PostMapping("/email-apply-all")
    @Operation(summary = "Send application emails to all shortlisted jobs with HR emails")
    public ResponseEntity<Map<String, Object>> emailApplyAll() {
        new Thread(() -> emailApplyService.applyAllByEmail()).start();
        return ResponseEntity.ok(Map.of("message", "Email applications started in background"));
    }

    @GetMapping("/follow-up")
    @Operation(summary = "Get applications due for 7-day follow-up")
    public ResponseEntity<List<Application>> followUp() {
        return ResponseEntity.ok(applicationService.getFollowUpDue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> get(@PathVariable Long id) {
        return applicationRepo.findById(id).map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
