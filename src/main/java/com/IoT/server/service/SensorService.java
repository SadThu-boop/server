package com.IoT.server.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.IoT.server.controller.api.response.DeviceResponse;
import com.IoT.server.controller.api.response.SensorResponse;
import com.IoT.server.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IoT.server.entity.SensorData;
import com.IoT.server.repository.SensorRepo;



@Service
public class SensorService {
    @Autowired
    private SensorRepo sensorRepo;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


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

    public List<SensorResponse> getSensorHistory() {
        List<SensorData> sensorData = sensorRepo.findAll(); // Trả về toàn bộ lịch sử bật/tắt thiết bị

        return sensorData.stream().map(sensor -> {
            SensorResponse dto = new SensorResponse();
            dto.setId(sensor.getId());
            dto.setTemp(sensor.getTemperature());
            dto.setHumid(sensor.getHumidity());
            dto.setLight(sensor.getLight());

            // Chuyển đổi LocalDateTime sang Date, sau đó format thành chuỗi
            String formattedTimestamp = dateFormat.format(
                    Date.from(sensor.getTimestamp().atZone(ZoneId.systemDefault()).toInstant())
            );
            dto.setTimestamp(formattedTimestamp);

            return dto;
        }).collect(Collectors.toList());
    }

    public SensorData getLatestData() {
        return sensorRepo.findTopByOrderByTimestampDesc(); // Trả về bản ghi mới nhất
    }

    public List<SensorData> findByTimestamp(String timestamp) {
        try {
            // Chuyển chuỗi thời gian thành Date
            Date date = dateFormat.parse(timestamp);

            // Chuyển đổi từ Date sang LocalDateTime
            LocalDateTime searchTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            // Tạo khoảng thời gian tìm kiếm: từ 0 đến 1 giây
            LocalDateTime startTime = searchTime.withNano(0); // Bắt đầu từ 0 nano giây
            LocalDateTime endTime = searchTime.plusSeconds(1).withNano(0); // Kết thúc vào giây tiếp theo

            // Tìm các bản ghi trong khoảng thời gian giữa startTime và endTime
            return sensorRepo.findByTimestampBetween(startTime, endTime);


        } catch (ParseException e) {
            // Xử lý lỗi nếu chuỗi thời gian không đúng định dạng
            throw new RuntimeException("Invalid date format, please use yyyy/MM/dd HH:mm:ss", e);
        }
    }
}
