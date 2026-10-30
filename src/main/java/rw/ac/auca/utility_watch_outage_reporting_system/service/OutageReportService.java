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
    public OutageReportService(OutageReportRepository repo,
            TechnicianRepository technicianRepo, SubscriberRepository subscriberRepo) {
        this.repo = repo;
        this.technicianRepo = technicianRepo;
        this.subscriberRepo = subscriberRepo;
    }
    // A2 save: links the real technician and subscriber. D1 replaces this method later.
    public String save(OutageReport body) {
        if (body.getTechnician() == null || body.getTechnician().getId() == null)
            return "The technician with that id does not exist";
        Optional<Technician> technician = technicianRepo.findById(body.getTechnician().getId());
        if (technician.isEmpty()) return "The technician with that id does not exist";
        if (body.getSubscriber() == null || body.getSubscriber().getId() == null)
            return "The subscriber with that id does not exist";
        Optional<Subscriber> subscriber = subscriberRepo.findById(body.getSubscriber().getId());
        if (subscriber.isEmpty()) return "The subscriber with that id does not exist";
        if (body.getStatus() == null) body.setStatus(OutageStatus.REPORTED);
        body.setId(null);
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
        // reportDate, subscriber and technician are not changed here, so a PUT cannot bypass D1.
        return Optional.of(repo.save(old));
    }
    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
    public List<OutageReport> byStatus(OutageStatus status) {
        return repo.findByStatusOrderByReportDateAsc(status);
    }
    public List<OutageReport> between(LocalDate start, LocalDate end) {
        return repo.findByReportDateBetweenOrderByReportDateAsc(start, end);
    }
}
