package com.jobautomation.app.service;

import com.jobautomation.app.model.Application;
import com.jobautomation.app.model.CandidateProfile;
import com.jobautomation.app.repository.ApplicationRepository;
import com.jobautomation.app.repository.JobListingRepository;
import com.jobautomation.app.repository.TargetCompanyRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor @Slf4j
public class EmailApplyService {

    private final JavaMailSender mailSender;
    private final ApplicationRepository applicationRepo;
    private final JobListingRepository jobListingRepo;
    private final TargetCompanyRepository targetCompanyRepo;

    @Value("${spring.mail.username:}") private String fromEmail;

    /**
     * Sends application email for a single application.
     * Returns true if email was sent successfully.
     */
    @Transactional
    public boolean applyByEmail(Application app) {
        String hrEmail = resolveHrEmail(app.getJobListing().getCompany());
        if (hrEmail == null) {
            log.info("No HR email for {} — skipping email apply", app.getJobListing().getCompany());
            return false;
        }

        try {
            CandidateProfile profile = app.getCandidate();
            String resumePath = "resumes/resume.pdf";

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom(fromEmail);
            helper.setTo(hrEmail);
            helper.setSubject("Application for " + app.getJobListing().getTitle() +
                              " — " + profile.getName() + " | Fresher Java Full-Stack + GenAI");
            helper.setText(app.getCoverLetter(), false);

            // Attach resume PDF
            ClassPathResource resume = new ClassPathResource(resumePath);
            if (resume.exists()) {
                helper.addAttachment("PAVANR-FULL STACK DEVELOPER -10-7-26.pdf", resume);
            }

            mailSender.send(msg);

            // Mark as APPLIED
            app.setStatus("APPLIED");
            app.setSubmittedAt(LocalDateTime.now());
            app.setAutoSubmitted(true);
            app.setNotes("Email sent to: " + hrEmail);
            applicationRepo.save(app);
            app.getJobListing().setStatus("APPLIED");
            jobListingRepo.save(app.getJobListing());

            log.info("EMAIL APPLIED: {} at {} → {}", app.getJobListing().getTitle(),
                     app.getJobListing().getCompany(), hrEmail);
            return true;

        } catch (Exception e) {
            log.warn("Email apply failed for {} at {}: {}",
                     app.getJobListing().getTitle(), app.getJobListing().getCompany(), e.getMessage());
            return false;
        }
    }

    /**
     * Sends emails for all SHORTLISTED applications that have an HR email.
     */
    @Transactional
    public int applyAllByEmail() {
        List<Application> shortlisted = applicationRepo.findByStatus("SHORTLISTED");
        int sent = 0;
        for (Application app : shortlisted) {
            if (applyByEmail(app)) sent++;
            // Small delay between emails to avoid spam filters
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        }
        return sent;
    }

    private String resolveHrEmail(String companyName) {
        return targetCompanyRepo.findAll().stream()
            .filter(c -> c.getName().equalsIgnoreCase(companyName) &&
                         c.getHrEmail() != null && !c.getHrEmail().isBlank())
            .map(c -> c.getHrEmail())
            .findFirst().orElse(null);
    }
}
