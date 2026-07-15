package com.jobautomation.app.service;

import com.jobautomation.app.model.Application;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String toEmail;

    public void sendDailySummary(List<Application> submitted, int shortlisted) {
        if (toEmail == null || toEmail.isBlank()) return;
        try {
            StringBuilder body = new StringBuilder();
            body.append("Job Application Automation — Daily Summary\n\n");
            body.append("Shortlisted today: ").append(shortlisted).append("\n");
            body.append("Auto-submitted: ").append(submitted.size()).append("\n\n");

            if (!submitted.isEmpty()) {
                body.append("Submitted Applications:\n");
                submitted.forEach(a -> body.append(String.format("  - %s at %s [%s] (Score: %.0f%%)\n",
                    a.getJobListing().getTitle(),
                    a.getJobListing().getCompany(),
                    a.getTrack(),
                    a.getMatchScore() * 100)));
            }

            body.append("\n⚠️ Review these in your dashboard: http://localhost:3000");

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("Job Automation: " + submitted.size() + " applications submitted today");
            msg.setText(body.toString());
            mailSender.send(msg);
            log.info("Daily summary email sent to {}", toEmail);
        } catch (Exception e) {
            log.warn("Failed to send daily summary email: {}", e.getMessage());
        }
    }

    public void sendFollowUpReminder(Application app) {
        if (toEmail == null || toEmail.isBlank()) return;
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("Follow-up reminder: " + app.getJobListing().getTitle() + " at " + app.getJobListing().getCompany());
            msg.setText("7 days have passed since you applied to " + app.getJobListing().getTitle() +
                        " at " + app.getJobListing().getCompany() + ". Consider following up.\n\nApply URL: " +
                        app.getJobListing().getApplyUrl());
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Failed to send follow-up reminder: {}", e.getMessage());
        }
    }
}
