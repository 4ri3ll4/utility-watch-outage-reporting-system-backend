package rw.ac.auca.utility_watch_outage_reporting_system.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;

public interface TechnicianRepository extends JpaRepository<Technician, UUID> {
    boolean existsByServiceZone(ServiceZone serviceZone);
    @Query("SELECT DISTINCT t FROM Technician t JOIN t.tradeSkills s " +
           "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Technician> findByTradeSkill(@Param("name") String name);
}
