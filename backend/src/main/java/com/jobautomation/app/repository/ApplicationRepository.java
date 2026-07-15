package com.jobautomation.app.repository;

import com.jobautomation.app.model.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByStatus(String status, Pageable pageable);
    List<Application> findByStatus(String status);
    boolean existsByJobListingId(Long jobListingId);
    List<Application> findByTrack(String track);
    long countByStatus(String status);
    long countByTrack(String track);
    long countByTrackAndStatus(String track, String status);

    @Query("SELECT a FROM Application a WHERE a.status = 'APPLIED' AND a.submittedAt < :cutoff AND a.followUpSent = false")
    List<Application> findFollowUpDue(LocalDateTime cutoff);

    @Query("SELECT a FROM Application a WHERE a.submittedAt >= :from ORDER BY a.submittedAt DESC")
    List<Application> findSubmittedSince(LocalDateTime from);

    @Query("SELECT DATE(a.submittedAt) as day, COUNT(a) as cnt FROM Application a WHERE a.submittedAt IS NOT NULL GROUP BY DATE(a.submittedAt) ORDER BY day DESC")
    List<Object[]> dailyApplicationCounts();
}
