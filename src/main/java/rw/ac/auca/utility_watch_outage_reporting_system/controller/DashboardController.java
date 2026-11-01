package rw.ac.auca.utility_watch_outage_reporting_system.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.*;
import rw.ac.auca.utility_watch_outage_reporting_system.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    public Map<String, Long> summary() {
        return service.summary();
    }
}