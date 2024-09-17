package com.IoT.server.controller.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.IoT.server.entity.SensorData;
import com.IoT.server.service.SensorService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class SensorDataController {
    @Autowired
    private SensorService sensorService;

    @Operation(summary = "Nhận dữ liệu từ cảm biến", description = "Nhận dữ liệu từ cảm biến và lưu vào cơ sở dữ liệu")
    @PostMapping("/sensor")
    public void recevieData(@RequestBody SensorData sensorData) {
        // Gọi hàm saveData từ SensorService
        sensorService.saveData(
                sensorData.getTemperature(), sensorData.getHumidity(), sensorData.getLight(), LocalDateTime.now());
    }

    @Operation(
            summary = "Lấy dữ liệu từ cảm biến",
            description = "Lấy dữ liệu từ cảm biến đã được lưu trong cơ sở dữ liệu")
    @GetMapping("/sensor")
    public List<SensorData> getAllData() {
        return sensorService.getAllData();
    }

    @Operation(
            summary = "Lấy dữ liệu mới nhất từ cảm biến",
            description = "Lấy dữ liệu mới nhất từ cảm biến đã được lưu trong cơ sở dữ liệu")
    @GetMapping("/sensor/latest")
    public SensorData getLatestData() {
        return sensorService.getLatestData();
    }
}
