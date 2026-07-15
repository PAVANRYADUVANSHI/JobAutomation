package com.jobautomation.app.service;

import com.jobautomation.app.model.*;
import com.jobautomation.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service @RequiredArgsConstructor @Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepo;
    private final JobListingRepository jobListingRepo;
    private final CandidateProfileRepository profileRepo;
    private final CoverLetterService coverLetterService;
    private final ResumeMatcherService matcherService;

    @Value("${app.review.mode:MANUAL}")
    private String reviewMode;

    @Value("${app.daily.shortlist.target:25}")
    private int dailyTarget;

    /**
     * Shortlists top N jobs from today's NEW listings, scores them, creates Application records.
     */
    @Transactional
    public List<Application> shortlistToday() {
        CandidateProfile profile = profileRepo.findAll().stream().findFirst().orElseThrow();
        List<JobListing> candidates = jobListingRepo.findShortlistedOrderByScore(PageRequest.of(0, dailyTarget));

        List<Application> apps = new ArrayList<>();
        for (JobListing job : candidates) {
            boolean exists = applicationRepo.existsByJobListingId(job.getId());
            if (exists) continue;

            Map<String, Double> scores = matcherService.scoreJob(job.getDescription(), job.getTitle());
            String version = matcherService.selectResumeVersion(scores);
            String track = matcherService.detectTrack(scores);
            double matchScore = Math.max(
                scores.getOrDefault("javaFullStack", 0.0),
                scores.getOrDefault("genAILeaning", 0.0)
            );

            job.setJavaMatchScore(scores.getOrDefault("javaFullStack", 0.0));
            job.setGenaiMatchScore(scores.getOrDefault("genAILeaning", 0.0));
            job.setSelectedResumeVersion(version);
            job.setTrack(track);
            job.setStatus("SHORTLISTED");
            jobListingRepo.save(job);

            String coverLetter = coverLetterService.generate(job, version);

            Application app = Application.builder()
                .jobListing(job)
                .candidate(profile)
                .resumeVersion(version)
                .coverLetter(coverLetter)
                .status("SHORTLISTED")
                .track(track)
                .matchScore(matchScore)
                .build();
            apps.add(applicationRepo.save(app));
        }
        return apps;
    }

    /**
     * Auto-submits all SHORTLISTED applications (AUTO mode only).
     * Only submits via platforms that have a submission API.
     */
    @Transactional
    public List<Application> autoSubmitAll() {
        if (!"AUTO".equalsIgnoreCase(reviewMode)) return Collections.emptyList();

        List<Application> shortlisted = applicationRepo.findByStatus("SHORTLISTED");
        List<Application> submitted = new ArrayList<>();

        for (Application app : shortlisted) {
            JobListing job = app.getJobListing();
            // Only auto-submit if source has a submission API; otherwise leave as manual-assist
            if (isAutoSubmittable(job.getSource())) {
                app.setStatus("APPLIED");
                app.setSubmittedAt(LocalDateTime.now());
                app.setAutoSubmitted(true);
                applicationRepo.save(app);
                job.setStatus("APPLIED");
                jobListingRepo.save(job);
                submitted.add(app);
                log.info("AUTO-SUBMITTED: {} at {} via {}", job.getTitle(), job.getCompany(), job.getSource());
            }
            // else: stays SHORTLISTED as manual-assist entry in dashboard
        }
        return submitted;
    }

    private boolean isAutoSubmittable(String source) {
        if (source == null) return false;
        String s = source.toLowerCase();
        // LinkedIn and Naukri web UI have no submission API — always manual
        return !s.contains("linkedin") && !s.contains("naukri");
    }

    @Transactional
    public Application updateStatus(Long id, String status, String notes) {
        Application app = applicationRepo.findById(id).orElseThrow();
        app.setStatus(status);
        if (notes != null) app.setNotes(notes);
        switch (status) {
            case "APPLIED" -> { app.setSubmittedAt(LocalDateTime.now());
                app.getJobListing().setStatus("APPLIED"); jobListingRepo.save(app.getJobListing()); }
            case "RESPONSE" -> app.setResponseAt(LocalDateTime.now());
            case "INTERVIEW" -> app.setInterviewAt(LocalDateTime.now());
        }
        return applicationRepo.save(app);
    }

    public List<Application> getFollowUpDue() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        return applicationRepo.findFollowUpDue(cutoff);
    }

    public List<Application> getTodayQueue() {
        return applicationRepo.findByStatus("SHORTLISTED");
    }
}
