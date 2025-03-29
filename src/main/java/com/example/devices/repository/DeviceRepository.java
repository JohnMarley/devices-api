package com.example.devices.repository;

import com.example.devices.entity.Device;
import com.example.devices.enums.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    @Query("SELECT d FROM Device d " +
            "WHERE (:brand IS NULL OR d.brand = :brand) " +
            "AND (:state IS NULL OR d.state = :state)")
    List<Device> findByBrandAndState(@Param("brand") String brand, @Param("state") State state);
}
