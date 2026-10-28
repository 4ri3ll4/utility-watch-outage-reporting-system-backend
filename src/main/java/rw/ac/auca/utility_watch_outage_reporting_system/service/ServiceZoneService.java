package rw.ac.auca.utility_watch_outage_reporting_system.service;

import java.util.*;
import org.springframework.stereotype.Service;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.repository.*;

@Service
public class ServiceZoneService {
    private final ServiceZoneRepository repo;
    public ServiceZoneService(ServiceZoneRepository repo) { this.repo = repo; }
    public ServiceZone save(ServiceZone value) { return repo.save(value); }
    public List<ServiceZone> all() { return repo.findAll(); }
    public Optional<ServiceZone> byId(UUID id) { return repo.findById(id); }
    public Optional<ServiceZone> update(UUID id, ServiceZone value) {
        Optional<ServiceZone> found = repo.findById(id);
        if (found.isEmpty()) return Optional.empty();
        ServiceZone old = found.get();
        old.setZoneNumber(value.getZoneNumber());
        old.setName(value.getName());
        return Optional.of(repo.save(old));
    }
    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
