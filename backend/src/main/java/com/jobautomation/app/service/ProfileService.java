package com.jobautomation.app.service;

import com.jobautomation.app.model.*;
import com.jobautomation.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class ProfileService {

    private final CandidateProfileRepository profileRepo;
    private final ResumeVersionRepository resumeVersionRepo;
    private final ResumeProjectRepository resumeProjectRepo;
    private final EducationRepository educationRepo;

    public CandidateProfile getProfile() {
        return profileRepo.findAll().stream().findFirst().orElseThrow();
    }

    public CandidateProfile updateReviewMode(String mode) {
        CandidateProfile p = getProfile();
        p.setReviewMode(mode.toUpperCase());
        return profileRepo.save(p);
    }

    public List<ResumeVersion> getResumeVersions() {
        CandidateProfile profile = getProfile();
        return resumeVersionRepo.findByCandidateId(profile.getId());
    }

    public List<ResumeProject> getProjectsForVersion(String versionKey) {
        return resumeVersionRepo.findAll().stream()
            .filter(rv -> rv.getVersionKey().equals(versionKey))
            .findFirst()
            .map(resumeProjectRepo::findByResumeVersion)
            .orElse(List.of());
    }
}
