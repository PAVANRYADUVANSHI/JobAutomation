package com.jobautomation.app.repository;

import com.jobautomation.app.model.ResumeProject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResumeProjectRepository extends JpaRepository<ResumeProject, Long> {
    List<ResumeProject> findByResumeVersionId(Long resumeVersionId);
    List<ResumeProject> findByResumeVersion(com.jobautomation.app.model.ResumeVersion resumeVersion);
}
