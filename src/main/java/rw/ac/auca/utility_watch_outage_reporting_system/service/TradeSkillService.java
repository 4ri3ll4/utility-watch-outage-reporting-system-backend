package rw.ac.auca.utility_watch_outage_reporting_system.service;

import java.util.*;
import org.springframework.stereotype.Service;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.repository.*;

@Service
public class TradeSkillService {
    private final TradeSkillRepository repo;
    public TradeSkillService(TradeSkillRepository repo) { this.repo = repo; }
    public TradeSkill save(TradeSkill value) { return repo.save(value); }
    public List<TradeSkill> all() { return repo.findAll(); }
    public Optional<TradeSkill> byId(UUID id) { return repo.findById(id); }
    public Optional<TradeSkill> update(UUID id, TradeSkill value) {
        Optional<TradeSkill> found = repo.findById(id);
        if (found.isEmpty()) return Optional.empty();
        TradeSkill old = found.get();
        old.setName(value.getName());
        return Optional.of(repo.save(old));
    }
    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
