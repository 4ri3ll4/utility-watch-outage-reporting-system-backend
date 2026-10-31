package rw.ac.auca.utility_watch_outage_reporting_system.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.repository.*;

@Service
public class OutageReportService {
    private final OutageReportRepository repo;
    private final TechnicianRepository technicianRepo;
    private final SubscriberRepository subscriberRepo;
    private final RepairActionRepository repairRepo;
    public OutageReportService(OutageReportRepository repo,
            TechnicianRepository technicianRepo, SubscriberRepository subscriberRepo, RepairActionRepository repairRepo) {
        this.repo = repo;
        this.technicianRepo = technicianRepo;
        this.subscriberRepo = subscriberRepo;
        this.repairRepo = repairRepo;
    }
    
    public String saveChecked(OutageReport body) {
        if (body.getReportDate() == null || body.getReportDate().isBefore(LocalDate.now()))
            return "Outage report date cannot be in the past";
        if (body.getTechnician() == null || body.getTechnician().getId() == null)
            return "The technician with that id does not exist";
        Optional<Technician> technician = technicianRepo.findById(body.getTechnician().getId());
        if (technician.isEmpty()) return "The technician with that id does not exist";
        if (body.getSubscriber() == null || body.getSubscriber().getId() == null)
            return "The subscriber with that id does not exist";
        Optional<Subscriber> subscriber = subscriberRepo.findById(body.getSubscriber().getId());
        if (subscriber.isEmpty()) return "The subscriber with that id does not exist";
        if (repo.existsByTechnicianIdAndReportDateAndStatusNot(
                technician.get().getId(), body.getReportDate(), OutageStatus.DISMISSED))
            return "Technician is already dispatched on that date";
        body.setId(null);
        body.setStatus(OutageStatus.REPORTED);
        body.setTechnician(technician.get());
        body.setSubscriber(subscriber.get());
        repo.save(body);
        return "Outage report saved successfully";
    }
    public List<OutageReport> all() { return repo.findAll(); }
    public Optional<OutageReport> byId(UUID id) { return repo.findById(id); }
    public Optional<OutageReport> update(UUID id, OutageReport value) {
        Optional<OutageReport> found = repo.findById(id);
        if (found.isEmpty()) return Optional.empty();
        OutageReport old = found.get();
        old.setUtilityType(value.getUtilityType());
        old.setDescription(value.getDescription());
        return Optional.of(repo.save(old));
    }
    public String changeStatus(UUID id, OutageStatus next) {
        Optional<OutageReport> found = repo.findById(id);
        if (found.isEmpty())
            return "The outage report with that id does not exist";
        OutageReport report = found.get();
        OutageStatus old = report.getStatus();
        boolean allowed = false;
        if (old == OutageStatus.REPORTED)
            allowed = next == OutageStatus.DISPATCHED || next == OutageStatus.DISMISSED;
        else if (old == OutageStatus.DISPATCHED)
            allowed = next == OutageStatus.RESOLVED || next == OutageStatus.DISMISSED;
        if (!allowed)
            return "Cannot change status from " + old + " to " + next;
        report.setStatus(next);
        repo.save(report);
        return "Outage report status updated successfully";
    }
    public String deleteChecked(UUID id) {
        Optional<OutageReport> report = repo.findById(id);
        if (report.isEmpty()) 
            return "The outage report with that id does not exist";
        if (repairRepo.existsByOutageReport(report.get()))
             return "That outage report has repair actions, delete them first";
        repo.deleteById(id);
        return "Outage report deleted successfully";
    }
    public List<OutageReport> byStatus(OutageStatus status) {
        return repo.findByStatusOrderByReportDateAsc(status);
    }
    public List<OutageReport> between(LocalDate start, LocalDate end) {
    return repo.findByReportDateBetweenOrderByReportDateAsc(start, end);
    }
}
