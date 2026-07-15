package com.jobautomation.app.scheduler;

import com.jobautomation.app.model.*;
import com.jobautomation.app.repository.SchedulerLogRepository;
import com.jobautomation.app.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Daily cron: fetch → match → shortlist → (if AUTO) submit → log → notify
 * Runs at 7:00 AM IST (01:30 UTC) every day.
 */
@Component @RequiredArgsConstructor @Slf4j
public class DailyJobScheduler {

    private final JobAggregatorService aggregatorService;
    private final ApplicationService applicationService;
    private final NotificationService notificationService;
    private final SchedulerLogRepository schedulerLogRepo;

    @Value("${app.review.mode:MANUAL}")
    private String reviewMode;

    @Scheduled(cron = "0 30 1 * * *") // 07:00 IST = 01:30 UTC
    public void runDailyPipeline() {
        log.info("=== Daily Job Pipeline START [mode={}] ===", reviewMode);
        LocalDateTime start = LocalDateTime.now();

        int fetched = 0;
        int shortlisted = 0;
        int submitted = 0;

        try {
            // Step 1: Fetch fresh listings from all sources
            fetched = aggregatorService.fetchAll();
            log.info("Fetched {} new job listings", fetched);

            // Step 2: Score + shortlist top 25
            List<Application> apps = applicationService.shortlistToday();
            shortlisted = apps.size();
            log.info("Shortlisted {} applications", shortlisted);

            // Step 3: Auto-submit if mode=AUTO
            List<Application> autoSubmitted = List.of();
            if ("AUTO".equalsIgnoreCase(reviewMode)) {
                autoSubmitted = applicationService.autoSubmitAll();
                submitted = autoSubmitted.size();
                log.info("Auto-submitted {} applications", submitted);
            }

            // Step 4: Follow-up reminders
            applicationService.getFollowUpDue().forEach(app -> {
                notificationService.sendFollowUpReminder(app);
                app.setFollowUpSent(true);
            });

            // Step 5: Daily summary email
            notificationService.sendDailySummary(autoSubmitted, shortlisted);

            // Step 6: Log run
            String summary = String.format("Fetched=%d Shortlisted=%d Submitted=%d Mode=%s",
                fetched, shortlisted, submitted, reviewMode);
            schedulerLogRepo.save(SchedulerLog.builder()
                .runAt(start)
                .jobsFetched(fetched)
                .jobsMatched(shortlisted)
                .jobsShortlisted(shortlisted)
                .jobsSubmitted(submitted)
                .mode(reviewMode)
                .summary(summary)
                .build());

        } catch (Exception e) {
            log.error("Daily pipeline failed: {}", e.getMessage(), e);
            schedulerLogRepo.save(SchedulerLog.builder()
                .runAt(start).mode(reviewMode)
                .summary("FAILED: " + e.getMessage())
                .build());
        }
        log.info("=== Daily Job Pipeline END ===");
    }
}
