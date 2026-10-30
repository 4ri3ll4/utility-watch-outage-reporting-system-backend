package rw.ac.auca.utility_watch_outage_reporting_system.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.service.*;

@RestController
@RequestMapping("/api/trade-skills")
public class TradeSkillController {
    private final TradeSkillService service;
    public TradeSkillController(TradeSkillService service) { this.service = service; }
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody TradeSkill body) {
        service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body("Trade skill saved successfully");
    }
    @GetMapping("/all")
    public List<TradeSkill> all() { return service.all(); }
    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable("id") UUID id) {
        Optional<TradeSkill> found = service.byId(id);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The trade skill with that id does not exist");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody TradeSkill body) {
        Optional<TradeSkill> found = service.update(id, body);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The trade skill with that id does not exist");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
        if (service.delete(id)) return ResponseEntity.ok("Trade skill deleted successfully");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The trade skill with that id does not exist");
    }
    @GetMapping("/unused")
    public List<TradeSkill> unused() {
        return service.unused();
    }
}
