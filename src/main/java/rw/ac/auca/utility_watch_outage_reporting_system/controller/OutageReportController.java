package rw.ac.auca.utility_watch_outage_reporting_system.controller;

import java.time.LocalDate;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.service.*;

@RestController
@RequestMapping("/api/outage-reports")
public class OutageReportController {
    private final OutageReportService service;
    public OutageReportController(OutageReportService service) { this.service = service; }
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody OutageReport body) {
        String result = service.save(body);
        if (result.equals("Outage report saved successfully"))
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }
    @GetMapping("/all")
    public List<OutageReport> all() { return service.all(); }
    @GetMapping("/{id}")
    public ResponseEntity<?> byId(@PathVariable("id") UUID id) {
        Optional<OutageReport> found = service.byId(id);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The outage report with that id does not exist");
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody OutageReport body) {
        Optional<OutageReport> found = service.update(id, body);
        if (found.isPresent()) return ResponseEntity.ok(found.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The outage report with that id does not exist");
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
        if (service.delete(id)) return ResponseEntity.ok("Outage report deleted successfully");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("The outage report with that id does not exist");
    }
    @GetMapping("/by-status")
    public List<OutageReport> byStatus(@RequestParam("status") OutageStatus status) {
        return service.byStatus(status);
    }
    @GetMapping("/between")
    public List<OutageReport> between(@RequestParam("start") String start,
                                    @RequestParam("end") String end) {
        return service.between(LocalDate.parse(start), LocalDate.parse(end));
    }
}
