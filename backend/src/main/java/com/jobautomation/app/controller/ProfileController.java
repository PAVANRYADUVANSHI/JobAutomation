package com.jobautomation.app.controller;

import com.jobautomation.app.model.*;
import com.jobautomation.app.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/profile")
@RequiredArgsConstructor @Tag(name = "Profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get candidate profile with resume versions")
    public ResponseEntity<Map<String, Object>> getProfile() {
        CandidateProfile profile = profileService.getProfile();
        List<ResumeVersion> versions = profileService.getResumeVersions();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("candidate", profile);
        response.put("resumeVersions", versions);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/review-mode")
    @Operation(summary = "Toggle REVIEW_MODE between MANUAL and AUTO")
    public ResponseEntity<CandidateProfile> updateReviewMode(@RequestParam String mode) {
        return ResponseEntity.ok(profileService.updateReviewMode(mode));
    }

    @GetMapping("/resume/{versionKey}/projects")
    @Operation(summary = "Get projects for a resume version")
    public ResponseEntity<List<ResumeProject>> getProjects(@PathVariable String versionKey) {
        return ResponseEntity.ok(profileService.getProjectsForVersion(versionKey));
    }
}
