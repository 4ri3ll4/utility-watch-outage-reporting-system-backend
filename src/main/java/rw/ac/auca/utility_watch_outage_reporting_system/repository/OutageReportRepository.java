package rw.ac.auca.utility_watch_outage_reporting_system.repository;

import java.util.*;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;

public interface OutageReportRepository extends JpaRepository<OutageReport, UUID> {
    List<OutageReport> findByStatusOrderByReportDateAsc(OutageStatus status);
    List<OutageReport> findByReportDateBetweenOrderByReportDateAsc(LocalDate start, LocalDate end);
    boolean existsByTechnicianIdAndReportDateAndStatusNot(UUID technicianId, LocalDate date, OutageStatus status);
    long countByReportDate(LocalDate date);
    long countByStatus(OutageStatus status);
    @Query("SELECT r.status, COUNT(r) FROM OutageReport r GROUP BY r.status")
    List<Object[]> countReportsByStatus();
    @Modifying
    @Query("UPDATE OutageReport r SET r.status = :dismissed " +
           "WHERE r.technician.id = :technicianId AND r.reportDate = :date " +
           "AND r.status <> :resolved")
    int dismissTechnicianDay(@Param("technicianId") UUID technicianId,
                             @Param("date") LocalDate date,
                             @Param("dismissed") OutageStatus dismissed,
                             @Param("resolved") OutageStatus resolved);
}
