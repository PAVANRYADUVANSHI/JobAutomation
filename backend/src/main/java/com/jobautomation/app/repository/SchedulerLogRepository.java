package com.jobautomation.app.repository;

import com.jobautomation.app.model.SchedulerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SchedulerLogRepository extends JpaRepository<SchedulerLog, Long> {
    List<SchedulerLog> findTop10ByOrderByRunAtDesc();
}
