package com.jobautomation.app.service;

import com.jobautomation.app.model.Application;
import com.jobautomation.app.model.CandidateProfile;
import com.jobautomation.app.repository.ApplicationRepository;
import com.jobautomation.app.repository.JobListingRepository;
import com.jobautomation.app.repository.TargetCompanyRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @Slf4j
public class EmailApplyService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired private ApplicationRepository applicationRepo;
    @Autowired private JobListingRepository jobListingRepo;
    @Autowired private TargetCompanyRepository targetCompanyRepo;

    @Value("${spring.mail.username:}") private String fromEmail;

    @Transactional
    public boolean applyByEmail(Application app) {
        if (mailSender == null || fromEmail == null || fromEmail.isBlank()) {
            log.warn("Mail not configured — cannot send email apply");
            return false;
        }
        String hrEmail = resolveHrEmail(app.getJobListing().getCompany());
        if (hrEmail == null) {
            log.info("No HR email for {} — skipping email apply", app.getJobListing().getCompany());
            return false;
        }
        try {
            CandidateProfile profile = app.getCandidate();
            ClassPathResource resume = new ClassPathResource("resumes/" + app.getResumeVersion() + "-resume.pdf");
            if (!resume.exists()) resume = new ClassPathResource("resumes/javafullstack-resume.pdf");
            if (!resume.exists()) resume = new ClassPathResource("resumes/resume.pdf");

            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setFrom(fromEmail);
            helper.setTo(hrEmail);
            helper.setSubject("Application for " + app.getJobListing().getTitle() +
                              " — " + profile.getName() + " | Fresher Java Full-Stack + GenAI");
            helper.setText(app.getCoverLetter(), false);
            if (resume.exists()) {
                helper.addAttachment(profile.getName().replace(" ", "-") + "-Resume.pdf", resume);
            }
            mailSender.send(msg);

            app.setStatus("APPLIED");
            app.setSubmittedAt(LocalDateTime.now());
            app.setAutoSubmitted(true);
            app.setNotes("Email sent to: " + hrEmail);
            applicationRepo.save(app);
            app.getJobListing().setStatus("APPLIED");
            jobListingRepo.save(app.getJobListing());
            log.info("EMAIL APPLIED: {} at {} -> {}", app.getJobListing().getTitle(),
                     app.getJobListing().getCompany(), hrEmail);
            return true;
        } catch (Exception e) {
            log.warn("Email apply failed for {} at {}: {}",
                     app.getJobListing().getTitle(), app.getJobListing().getCompany(), e.getMessage());
            return false;
        }
    }

    @Transactional
    public int applyAllByEmail() {
        List<Application> shortlisted = applicationRepo.findByStatus("SHORTLISTED");
        int sent = 0;
        for (Application app : shortlisted) {
            if (applyByEmail(app)) sent++;
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
