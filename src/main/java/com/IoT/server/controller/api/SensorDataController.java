package com.IoT.server.controller.api;

import java.time.LocalDateTime;
import java.util.List;

import com.IoT.server.controller.api.response.SensorResponse;
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


    @PostMapping("/sensor")
    public void recevieData(@RequestBody SensorData sensorData) {
        // Gọi hàm saveData từ SensorService
        sensorService.saveData(
                sensorData.getTemperature(), sensorData.getHumidity(), sensorData.getLight(), LocalDateTime.now());
    }


    @GetMapping("/sensor")
    public List<SensorResponse> getAllData() {
        return sensorService.getSensorHistory();
    }


    @GetMapping("/sensor/latest")
    public SensorData getLatestData() {
        return sensorService.getLatestData();
    }

    @GetMapping("/sensor/findByTime")
    public List<SensorData> getDataByTime(@RequestParam(required = false) String timestamp) {
        if(timestamp !=null && !timestamp.isEmpty()) {
            // Gọi service để tìm kiếm các bản ghi
            return sensorService.findByTimestamp(timestamp);
        }
        else {
            return sensorService.getAllData();
        }
    }
}
