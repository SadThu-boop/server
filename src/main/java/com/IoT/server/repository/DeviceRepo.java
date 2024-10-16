package com.IoT.server.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.IoT.server.controller.api.response.DeviceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.IoT.server.entity.Device;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Integer> {
    Optional<Device> findByName(String name);

    List<Device> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

}
