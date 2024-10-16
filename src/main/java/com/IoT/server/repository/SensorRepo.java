package com.IoT.server.repository;

import com.IoT.server.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.IoT.server.entity.SensorData;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorRepo extends JpaRepository<SensorData, Integer> {
    SensorData findTopByOrderByTimestampDesc();

    List<SensorData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
