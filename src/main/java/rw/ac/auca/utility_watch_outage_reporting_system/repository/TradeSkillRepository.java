package rw.ac.auca.utility_watch_outage_reporting_system.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;

public interface TradeSkillRepository extends JpaRepository<TradeSkill, UUID> {
    @Query("SELECT t FROM TradeSkill t WHERE t.technicians IS EMPTY")
    List<TradeSkill> findUnused();
}
