package com.IoT.server.repository;

import com.IoT.server.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Integer> {
    Optional<Device> findByName(String name);
}
