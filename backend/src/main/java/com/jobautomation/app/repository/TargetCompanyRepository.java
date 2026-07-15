package com.jobautomation.app.repository;

import com.jobautomation.app.model.TargetCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TargetCompanyRepository extends JpaRepository<TargetCompany, Long> {
    List<TargetCompany> findByActive(boolean active);
    List<TargetCompany> findByActiveTrue();
    List<TargetCompany> findByActiveTrueAndIsManualWatchlistFalse();
    List<TargetCompany> findByActiveTrueAndIsManualWatchlistTrue();
    List<TargetCompany> findByActiveTrueAndAtsType(String atsType);
}
