package com.IoT.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh ID
    private int id;

    private float temperature;
    private float humidity;
    private float light;

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime timestamp; // Timestamp
}
