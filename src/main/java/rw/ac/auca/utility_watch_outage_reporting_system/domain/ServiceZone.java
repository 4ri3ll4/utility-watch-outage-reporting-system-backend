package rw.ac.auca.utility_watch_outage_reporting_system.domain;

import java.util.UUID;
import jakarta.persistence.*;
 
@Entity
@Table(name = "service_zone")
public class ServiceZone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int zoneNumber;
    private String name;
 
    public ServiceZone() {}
 
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public int getZoneNumber() { return zoneNumber; }
    public void setZoneNumber(int zoneNumber) { this.zoneNumber = zoneNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
