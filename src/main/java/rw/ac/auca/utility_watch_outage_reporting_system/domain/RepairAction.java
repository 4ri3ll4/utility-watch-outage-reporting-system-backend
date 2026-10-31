package rw.ac.auca.utility_watch_outage_reporting_system.domain;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "repair_action")
public class RepairAction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String actionName;
    private String notes;
    private int hoursTaken;
    private LocalDate actionDate;
    @ManyToOne
    @JoinColumn(name = "outage_report_id")
    private OutageReport outageReport;

    public RepairAction() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getActionName() { return actionName; }
    public void setActionName(String actionName) { this.actionName = actionName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public int getHoursTaken() { return hoursTaken; }
    public void setHoursTaken(int hoursTaken) { this.hoursTaken = hoursTaken; }
    public LocalDate getActionDate() { return actionDate; }
    public void setActionDate(LocalDate actionDate) { this.actionDate = actionDate; }
    public OutageReport getOutageReport() { return outageReport; }
    public void setOutageReport(OutageReport outageReport) { this.outageReport = outageReport; }
}