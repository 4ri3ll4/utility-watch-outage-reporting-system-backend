package rw.ac.auca.utility_watch_outage_reporting_system.controller;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;
import rw.ac.auca.utility_watch_outage_reporting_system.service.*;

@RestController
@RequestMapping("/api/repair-actions")
public class RepairActionController {
    private final RepairActionService service;

    public RepairActionController(RepairActionService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam("outageReportId") UUID id,
                                       @RequestBody RepairAction action) {
        String result = service.save(action, id);
        if (result.equals("The outage report with that id does not exist"))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        else if (result.equals("Repair actions are only for RESOLVED outage reports"))
            return ResponseEntity.badRequest().body(result);
        else
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/subscriber/{meterNumber}")
    public List<RepairAction> history(@PathVariable("meterNumber") String meterNumber) {
        return service.bySubscriber(meterNumber);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
        if (service.delete(id))
            return ResponseEntity.ok("Repair action deleted successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The repair action with that id does not exist");
    }
}