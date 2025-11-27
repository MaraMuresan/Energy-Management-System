package com.example.monitoringmicroservice.repositories;

import com.example.monitoringmicroservice.entities.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<HourlyConsumption> findByDeviceId(UUID deviceId);

    /**
     * Example: Write Custom Query
     */
    @Query(value = "SELECT h " +
            "FROM HourlyConsumption h " +
            "WHERE h.deviceId = :deviceId " +
            "AND h.date = :date ")
    List<HourlyConsumption> findByDeviceIdAndDate(@Param("deviceId") UUID deviceId,
                                                  @Param("date") LocalDate date);

    @Query(value = "SELECT h " +
            "FROM HourlyConsumption h " +
            "WHERE h.deviceId = :deviceId " +
            "AND h.date = :date " +
            " AND h.hour = :hour ")
    Optional<HourlyConsumption> findSpecificHour(
            @Param("deviceId") UUID deviceId,
            @Param("date") LocalDate date,
            @Param("hour") int hour);
}
