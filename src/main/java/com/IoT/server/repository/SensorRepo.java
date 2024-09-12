package com.IoT.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.IoT.server.entity.SensorData;

@Repository
public interface SensorRepo extends JpaRepository<SensorData, Integer> {
    SensorData findTopByOrderByTimestampDesc();
}
