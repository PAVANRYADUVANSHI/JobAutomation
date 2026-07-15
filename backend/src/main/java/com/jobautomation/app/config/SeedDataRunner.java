package com.jobautomation.app.config;

import com.jobautomation.app.model.AppUser;
import com.jobautomation.app.repository.AppUserRepository;
import com.jobautomation.app.repository.CandidateProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Creates a default admin user on first run if none exists.
 * Flyway handles all DB seed data (V2 migration).
 */
@Component @RequiredArgsConstructor @Slf4j
public class SeedDataRunner implements CommandLineRunner {

    private final AppUserRepository userRepo;
    private final CandidateProfileRepository profileRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (!userRepo.existsByUsername("pavan")) {
            userRepo.save(AppUser.builder()
                .username("pavan")
                .password(encoder.encode("pavan123"))
                .role("ROLE_USER")
                .build());
            log.info("Default user created: pavan / pavan123 — CHANGE THIS PASSWORD!");
        }
        long profileCount = profileRepo.count();
        log.info("Startup check: {} candidate profile(s) loaded", profileCount);
    }
}
