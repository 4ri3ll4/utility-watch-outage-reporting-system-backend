package rw.ac.auca.utility_watch_outage_reporting_system.domain;

import java.util.UUID;
import jakarta.persistence.*;
import java.time.LocalDate;
 
@Entity
@Table(name = "outage_report")
public class OutageReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDate reportDate;
    @Enumerated(EnumType.STRING)
    private OutageStatus status;
    private String utilityType;
    private String description;
    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private Subscriber subscriber;
    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;
 
    public OutageReport() {}
 
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
    public OutageStatus getStatus() { return status; }
    public void setStatus(OutageStatus status) { this.status = status; }
    public String getUtilityType() { return utilityType; }
    public void setUtilityType(String utilityType) { this.utilityType = utilityType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Subscriber getSubscriber() { return subscriber; }
    public void setSubscriber(Subscriber subscriber) { this.subscriber = subscriber; }
    public Technician getTechnician() { return technician; }
    public void setTechnician(Technician technician) { this.technician = technician; }
}
