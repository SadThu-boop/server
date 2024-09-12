package com.IoT.server.repository;

import com.IoT.server.entity.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepo extends JpaRepository<SensorData, Integer> {
    SensorData findTopByOrderByTimestampDesc();

}
