package rw.ac.auca.utility_watch_outage_reporting_system.service;

import java.util.*;
import org.springframework.stereotype.Service;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.repository.*;

@Service
public class TechnicianService {
    private final TechnicianRepository repo;
    private final ServiceZoneRepository zoneRepo;
    private final TradeSkillRepository skillRepo;
    public TechnicianService(TechnicianRepository repo,
            ServiceZoneRepository zoneRepo, TradeSkillRepository skillRepo) {
        this.repo = repo;
        this.zoneRepo = zoneRepo;
        this.skillRepo = skillRepo;
    }
    // A2 save: the JSON only holds ids for the zone and skills, so replace them with the real rows.
    public Technician save(Technician value) {
        if (value.getServiceZone() != null && value.getServiceZone().getId() != null) {
            Optional<ServiceZone> zone = zoneRepo.findById(value.getServiceZone().getId());
            if (zone.isPresent()) value.setServiceZone(zone.get());
            else value.setServiceZone(null); // use an existing zone id in Postman
        }
        List<TradeSkill> realSkills = new ArrayList<>();
        if (value.getTradeSkills() != null) {
            for (TradeSkill skill : value.getTradeSkills()) {
                if (skill.getId() != null) {
                    Optional<TradeSkill> found = skillRepo.findById(skill.getId());
                    if (found.isPresent()) realSkills.add(found.get());
                }
            }
        }
        value.setTradeSkills(realSkills);
        value.setId(null);
        return repo.save(value);
    }
    public List<Technician> all() { return repo.findAll(); }
    public Optional<Technician> byId(UUID id) { return repo.findById(id); }
    public Optional<Technician> update(UUID id, Technician value) {
        Optional<Technician> found = repo.findById(id);
        if (found.isEmpty()) return Optional.empty();
        Technician old = found.get();
        old.setFirstName(value.getFirstName());
        old.setLastName(value.getLastName());
        old.setPhone(value.getPhone());
        // the zone and the skills are NOT copied here: they change only with D4 and D3,
        // so an ordinary PUT can never wipe a technician’s skills or zone.
        return Optional.of(repo.save(old));
    }
    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
}
