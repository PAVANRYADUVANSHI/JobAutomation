package com.jobautomation.app.repository;

import com.jobautomation.app.model.JobListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    Optional<JobListing> findByDedupHash(String hash);
    Page<JobListing> findByStatus(String status, Pageable pageable);
    List<JobListing> findByStatusAndTrack(String status, String track);

    @Query(value = "SELECT * FROM job_listing WHERE status = 'SHORTLISTED' ORDER BY GREATEST(java_match_score, genai_match_score) DESC LIMIT :#{#pageable.pageSize}", nativeQuery = true)
    List<JobListing> findShortlistedOrderByScore(Pageable pageable);

    long countByTrack(String track);
    long countByStatus(String status);
}
