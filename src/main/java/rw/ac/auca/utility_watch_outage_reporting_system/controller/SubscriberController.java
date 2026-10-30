package rw.ac.auca.utility_watch_outage_reporting_system.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.service.*;

@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {
    private final SubscriberService service;
    public SubscriberController(SubscriberService service) { this.service = service; }
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Subscriber body) {
        service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscriber saved successfully");
    }
    @GetMapping("/all")
    public List<Subscriber> all() { return service.all(); }
    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable("id") UUID id) {
        Optional<Subscriber> found = service.byId(id);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The subscriber with that id does not exist");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody Subscriber body) {
        Optional<Subscriber> found = service.update(id, body);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The subscriber with that id does not exist");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
        if (service.delete(id)) return ResponseEntity.ok("Subscriber deleted successfully");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The subscriber with that id does not exist");
    }
    @GetMapping("/by-last-name")
    public List<Subscriber> byLastName(@RequestParam("lastName") String lastName) {
        return service.byLastName(lastName);
    }
    @GetMapping("/of-technician/{technicianId}")
    public ResponseEntity<?> ofTechnician(@PathVariable("technicianId") UUID id) {
        if (!service.technicianExists(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The technician with that id does not exist");
        }
        return ResponseEntity.ok(service.ofTechnician(id));
    }
}
