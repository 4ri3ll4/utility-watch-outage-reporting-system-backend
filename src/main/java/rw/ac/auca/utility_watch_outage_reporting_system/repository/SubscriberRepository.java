package rw.ac.auca.utility_watch_outage_reporting_system.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;

public interface SubscriberRepository extends JpaRepository<Subscriber, UUID> {
    List<Subscriber> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);
    @Query("SELECT DISTINCT r.subscriber FROM OutageReport r " +
           "WHERE r.technician.id = :technicianId")
    List<Subscriber> findSubscribersOfTechnician(@Param("technicianId") UUID technicianId);
}
