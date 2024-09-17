package com.IoT.server.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@RequiredArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private boolean status;

    private LocalDateTime timestamp;
}
