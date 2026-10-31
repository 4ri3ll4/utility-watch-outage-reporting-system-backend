package rw.ac.auca.utility_watch_outage_reporting_system.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.service.*;

@RestController
@RequestMapping("/api/technicians")
public class TechnicianController {
    private final TechnicianService service;
    public TechnicianController(TechnicianService service) { this.service = service; }
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Technician body) {
        service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body("Technician saved successfully");
    }
    @GetMapping("/all")
    public List<Technician> all() { return service.all(); }
    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable("id") UUID id) {
        Optional<Technician> found = service.byId(id);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The technician with that id does not exist");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody Technician body) {
        Optional<Technician> found = service.update(id, body);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The technician with that id does not exist");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
        if (service.delete(id)) return ResponseEntity.ok("Technician deleted successfully");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The technician with that id does not exist");
    }
    @GetMapping("/by-trade-skill")
    public List<Technician> byTradeSkill(@RequestParam("name") String name) {
        return service.byTradeSkill(name);
    }
    @PutMapping("/{technicianId}/trade-skills/{tradeSkillId}")
    public ResponseEntity<String> addTradeSkill(
            @PathVariable("technicianId") UUID technicianId,
            @PathVariable("tradeSkillId") UUID tradeSkillId) {
        String result = service.addTradeSkill(technicianId, tradeSkillId);
        if (result.equals("The technician with that id does not exist")
                || result.equals("That trade skill does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        else if (result.equals("The technician already has that trade skill"))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        else
            return ResponseEntity.ok(result);
    }
    @PatchMapping("/{technicianId}/service-zone")
    public ResponseEntity<String> moveZone(@PathVariable("technicianId") UUID id,
                                        @RequestParam("zoneNumber") int zoneNumber) {
        String result = service.moveZone(id, zoneNumber);
        if (result.equals("The technician with that id does not exist")
                || result.equals("The service zone with that zoneNumber does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        else if (result.equals("That service zone is taken by another technician"))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        else
            return ResponseEntity.ok(result);
    }
}
