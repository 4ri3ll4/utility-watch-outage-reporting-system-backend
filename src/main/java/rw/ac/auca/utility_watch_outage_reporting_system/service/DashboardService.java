package rw.ac.auca.utility_watch_outage_reporting_system.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.OutageStatus;
import rw.ac.auca.utility_watch_outage_reporting_system.repository.*;

@Service
public class DashboardService {
    private final SubscriberRepository subscribers;
    private final TechnicianRepository technicians;
    private final ServiceZoneRepository zones;
    private final OutageReportRepository reports;
    private final RepairActionRepository actions;

    public DashboardService(SubscriberRepository subscribers,
                            TechnicianRepository technicians,
                            ServiceZoneRepository zones,
                            OutageReportRepository reports,
                            RepairActionRepository actions) {
        this.subscribers = subscribers;
        this.technicians = technicians;
        this.zones = zones;
        this.reports = reports;
        this.actions = actions;
    }

    public Map<String, Long> summary() {
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("subscribers", subscribers.count());
        result.put("technicians", technicians.count());
        result.put("freeZones", (long) zones.findFreeZones().size());
        result.put("reportsToday", reports.countByReportDate(LocalDate.now()));
        result.put("reported", reports.countByStatus(OutageStatus.REPORTED));
        result.put("repairActions", actions.count());
        return result;
    }
}