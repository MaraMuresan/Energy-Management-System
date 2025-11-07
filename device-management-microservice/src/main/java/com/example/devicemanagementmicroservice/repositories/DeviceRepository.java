package com.example.devicemanagementmicroservice.repositories;

import com.example.devicemanagementmicroservice.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    /**
     * Example: JPA generate Query by Field
     */
    List<Device> findByName(String name);

    /**
     * Example: Write Custom Query
     */
    @Query(value = "SELECT d " +
            "FROM Device d " +
            "WHERE d.name = :name " +
            "AND d.yearOfManufacture >= 2022  ")
    Optional<Device> findNewDevicesByName(@Param("name") String name);
}
