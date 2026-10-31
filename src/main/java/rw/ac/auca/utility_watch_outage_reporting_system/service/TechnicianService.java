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

    public Technician save(Technician value) {
        if (value.getServiceZone() != null && value.getServiceZone().getId() != null) {
            Optional<ServiceZone> zone = zoneRepo.findById(value.getServiceZone().getId());
            if (zone.isPresent()) value.setServiceZone(zone.get());
            else value.setServiceZone(null);
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

        return Optional.of(repo.save(old));
    }
    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }
    public List<Technician> byTradeSkill(String name) {
        return repo.findByTradeSkill(name);
    }
    public String addTradeSkill(UUID technicianId, UUID tradeSkillId) {
        Optional<Technician> technician = repo.findById(technicianId);
        if (technician.isEmpty())
            return "The technician with that id does not exist";
        Optional<TradeSkill> skill = skillRepo.findById(tradeSkillId);
        if (skill.isEmpty())
            return "That trade skill does not exist";
        if (technician.get().getTradeSkills().contains(skill.get()))
            return "The technician already has that trade skill";
        technician.get().getTradeSkills().add(skill.get());
        repo.save(technician.get());
        return "Trade skill added to the technician";
    }
    public String moveZone(UUID technicianId, int zoneNumber) {
        Optional<Technician> technician = repo.findById(technicianId);
        if (technician.isEmpty())
            return "The technician with that id does not exist";
        Optional<ServiceZone> zone = zoneRepo.findByZoneNumber(zoneNumber);
        if (zone.isEmpty())
            return "The service zone with that zoneNumber does not exist";
        if (repo.existsByServiceZone(zone.get())
                && (technician.get().getServiceZone() == null
                    || !technician.get().getServiceZone().getId().equals(zone.get().getId())))
            return "That service zone is taken by another technician";
        technician.get().setServiceZone(zone.get());
        repo.save(technician.get());
        return "Service zone changed successfully";
    }
}
