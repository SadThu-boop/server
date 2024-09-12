package com.IoT.server.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.IoT.server.entity.Device;

@Repository
public interface DeviceRepo extends JpaRepository<Device, Integer> {
    Optional<Device> findByName(String name);
}
