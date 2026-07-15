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

    @Query(value = "SELECT CAST(submitted_at AS DATE) as day, COUNT(*) as cnt FROM application WHERE submitted_at IS NOT NULL GROUP BY CAST(submitted_at AS DATE) ORDER BY day DESC", nativeQuery = true)
    List<Object[]> dailyApplicationCounts();
}
