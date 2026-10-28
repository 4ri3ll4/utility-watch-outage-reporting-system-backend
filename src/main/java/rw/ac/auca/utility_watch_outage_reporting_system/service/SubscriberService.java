package rw.ac.auca.utility_watch_outage_reporting_system.service;

import java.util.*;
import org.springframework.stereotype.Service;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.repository.*;

@Service
public class SubscriberService {
    private final SubscriberRepository repo;
    public SubscriberService(SubscriberRepository repo) { this.repo = repo; }
    public Subscriber save(Subscriber value) { return repo.save(value); }
    public List<Subscriber> all() { return repo.findAll(); }
    public Optional<Subscriber> byId(UUID id) { return repo.findById(id); }
    public Optional<Subscriber> update(UUID id, Subscriber value) {
        Optional<Subscriber> found = repo.findById(id);
        if (found.isEmpty()) return Optional.empty();
        Subscriber old = found.get();
        old.setFirstName(value.getFirstName());
        old.setLastName(value.getLastName());
        old.setMeterNumber(value.getMeterNumber());
        old.setPhone(value.getPhone());
        old.setAddress(value.getAddress());
        return Optional.of(repo.save(old));
    }
    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
