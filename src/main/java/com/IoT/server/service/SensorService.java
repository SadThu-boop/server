package com.IoT.server.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IoT.server.entity.SensorData;
import com.IoT.server.repository.SensorRepo;

@Service
public class SensorService {
    @Autowired
    private SensorRepo sensorRepo;

    public void saveData(float temperature, float humidity, float light, LocalDateTime timestamp) {
        SensorData sensorData = new SensorData();

        sensorData.setTemperature(temperature);
        sensorData.setHumidity(humidity);
        sensorData.setLight(light);
        sensorData.setTimestamp(timestamp);

        sensorRepo.save(sensorData); // Lưu bản ghi mới
    }

    public List<SensorData> getAllData() {
        return sensorRepo.findAll(); // Trả về toàn bộ dữ liệu
    }

    public SensorData getLatestData() {
        return sensorRepo.findTopByOrderByTimestampDesc(); // Trả về bản ghi mới nhất
    }
}
