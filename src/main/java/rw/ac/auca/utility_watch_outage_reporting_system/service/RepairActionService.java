package rw.ac.auca.utility_watch_outage_reporting_system.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.repository.*;

@Service
public class RepairActionService {
    private final RepairActionRepository repo;
    private final OutageReportRepository reportRepo;

    public RepairActionService(RepairActionRepository repo, OutageReportRepository reportRepo) {
        this.repo = repo;
        this.reportRepo = reportRepo;
    }

    public String save(RepairAction action, UUID reportId) {
        Optional<OutageReport> report = reportRepo.findById(reportId);
        if (report.isEmpty())
            return "The outage report with that id does not exist";
        if (report.get().getStatus() != OutageStatus.RESOLVED)
            return "Repair actions are only for RESOLVED outage reports";
        action.setId(null);
        action.setActionDate(LocalDate.now());
        action.setOutageReport(report.get());
        repo.save(action);
        return "Repair action saved successfully";
    }

    public List<RepairAction> bySubscriber(String meterNumber) {
        return repo.findBySubscriberMeterNumber(meterNumber);
    }

    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}