package rw.ac.auca.utility_watch_outage_reporting_system.domain;

import java.util.UUID;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
 
@Entity
@Table(name = "technician")
public class Technician {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    @ManyToOne
    @JoinColumn(name = "service_zone_id")
    private ServiceZone serviceZone;
    @ManyToMany
    @JoinTable(name = "technician_trade_skill", joinColumns = @JoinColumn(name = "technician_id"), inverseJoinColumns = @JoinColumn(name = "trade_skill_id"))
    private List<TradeSkill> tradeSkills = new ArrayList<>();
 
    public Technician() {}
 
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public ServiceZone getServiceZone() { return serviceZone; }
    public void setServiceZone(ServiceZone serviceZone) { this.serviceZone = serviceZone; }
    public List<TradeSkill> getTradeSkills() { return tradeSkills; }
    public void setTradeSkills(List<TradeSkill> tradeSkills) { this.tradeSkills = tradeSkills; }
}
