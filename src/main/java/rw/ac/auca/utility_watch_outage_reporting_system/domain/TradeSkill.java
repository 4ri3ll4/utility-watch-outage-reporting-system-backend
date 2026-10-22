package rw.ac.auca.utility_watch_outage_reporting_system.domain;

import java.util.UUID;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
 
@Entity
@Table(name = "trade_skill")
public class TradeSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    @ManyToMany(mappedBy = "tradeSkills")
    @JsonIgnore
    private List<Technician> technicians = new ArrayList<>();
 
    public TradeSkill() {}
 
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Technician> getTechnicians() { return technicians; }
    public void setTechnicians(List<Technician> technicians) { this.technicians = technicians; }
}
