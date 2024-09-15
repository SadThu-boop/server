package com.IoT.server.controller.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.IoT.server.entity.SensorData;
import com.IoT.server.service.SensorService;

@RestController
@RequestMapping("/api")
public class SensorDataController {
    @Autowired
    private SensorService sensorService;

    @PostMapping("/sensor")
    public void recevieData(@RequestBody SensorData sensorData) {
        // Gọi hàm saveData từ SensorService
        sensorService.saveData(
                sensorData.getTemperature(), sensorData.getHumidity(), sensorData.getLight(), LocalDateTime.now());
    }

    @GetMapping("/sensor")
    public List<SensorData> getAllData() {
        return sensorService.getAllData();
    }

    @GetMapping("/sensor/latest")
    public SensorData getLatestData() {
        return sensorService.getLatestData();
    }
}
