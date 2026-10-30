package rw.ac.auca.utility_watch_outage_reporting_system.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.service.*;

@RestController
@RequestMapping("/api/service-zones")
public class ServiceZoneController {
    private final ServiceZoneService service;
    public ServiceZoneController(ServiceZoneService service) { this.service = service; }
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody ServiceZone body) {
        service.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body("Service zone saved successfully");
    }
    @GetMapping("/all")
    public List<ServiceZone> all() { return service.all(); }
    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable("id") UUID id) {
        Optional<ServiceZone> found = service.byId(id);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The service zone with that id does not exist");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody ServiceZone body) {
        Optional<ServiceZone> found = service.update(id, body);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The service zone with that id does not exist");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
        if (service.delete(id)) return ResponseEntity.ok("Service zone deleted successfully");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The service zone with that id does not exist");
    }
    @GetMapping("/free")
    public List<ServiceZone> free() {
        return service.free();
    }
}
