package com.jobautomation.app.repository;

import com.jobautomation.app.model.ResumeVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ResumeVersionRepository extends JpaRepository<ResumeVersion, Long> {
    List<ResumeVersion> findByCandidateId(Long candidateId);
    Optional<ResumeVersion> findByCandidateIdAndVersionKey(Long candidateId, String versionKey);
}
