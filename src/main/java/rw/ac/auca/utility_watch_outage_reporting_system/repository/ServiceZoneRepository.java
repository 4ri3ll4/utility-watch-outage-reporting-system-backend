package rw.ac.auca.utility_watch_outage_reporting_system.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;

public interface ServiceZoneRepository extends JpaRepository<ServiceZone, UUID> {
    Optional<ServiceZone> findByZoneNumber(int zoneNumber);
    @Query("SELECT z FROM ServiceZone z WHERE NOT EXISTS " +
           "(SELECT t FROM Technician t WHERE t.serviceZone = z) ORDER BY z.zoneNumber ASC")
    List<ServiceZone> findFreeZones();
}
