package rw.ac.auca.utility_watch_outage_reporting_system.repository;

import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import rw.ac.auca.utility_watch_outage_reporting_system.domain.*;

public interface RepairActionRepository extends JpaRepository<RepairAction, UUID> {

    boolean existsByOutageReport(OutageReport outageReport);

    @Query("SELECT a FROM RepairAction a WHERE " +
           "a.outageReport.subscriber.meterNumber = :meterNumber " +
           "ORDER BY a.actionDate DESC")
    List<RepairAction> findBySubscriberMeterNumber(@Param("meterNumber") String meterNumber);
}