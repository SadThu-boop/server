package com.IoT.server.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.IoT.server.controller.api.response.DeviceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.IoT.server.entity.Device;
import com.IoT.server.repository.DeviceRepo;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepo deviceRepo;

    @Autowired
    private MQTTService mqttService;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Device recordDeviceStatus(String name, boolean status) {
        Device device = new Device();

        device.setName(name);
        device.setStatus(status);
        device.setTimestamp(LocalDateTime.now()); // Ghi lại thời gian bật/tắt
        return deviceRepo.save(device);
    }

    public String formatActionTime(Date actionTime) {
        return dateFormat.format(actionTime); // Định dạng thời gian theo YYYY/MM/DD HH:MM:SS
    }



    public void recordDeviceHistory(String deviceName, boolean status) {
        Device device = new Device();
        device.setName(deviceName);
        device.setStatus(status);
        device.setTimestamp(LocalDateTime.now());

        deviceRepo.save(device);
    }


    public List<DeviceResponse> getDeviceHistory() {
        List<Device> devices = deviceRepo.findAll(); // Trả về toàn bộ lịch sử bật/tắt thiết bị

        return devices.stream().map(device -> {
            DeviceResponse dto = new DeviceResponse();
            dto.setId(device.getId());
            dto.setName(device.getName());
            dto.setStatus(device.getStatus());

            // Chuyển đổi LocalDateTime sang Date, sau đó format thành chuỗi
            String formattedTimestamp = dateFormat.format(
                    Date.from(device.getTimestamp().atZone(ZoneId.systemDefault()).toInstant())
            );
            dto.setTimestamp(formattedTimestamp);

            return dto;
        }).collect(Collectors.toList());
    }



    public List<Device> getAll() {
        return deviceRepo.findAll();
    }

    public List<Device> findByTimestamp(String timestamp) {
        try {
            // Chuyển chuỗi thời gian thành Date
            Date date = dateFormat.parse(timestamp);

            // Chuyển đổi từ Date sang LocalDateTime
            LocalDateTime searchTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            // Tạo khoảng thời gian tìm kiếm: từ 0 đến 1 giây
            LocalDateTime startTime = searchTime.withNano(0); // Bắt đầu từ 0 nano giây
            LocalDateTime endTime = searchTime.plusSeconds(1).withNano(0); // Kết thúc vào giây tiếp theo

            // Tìm các bản ghi trong khoảng thời gian giữa startTime và endTime
            return deviceRepo.findByTimestampBetween(startTime, endTime);

        } catch (ParseException e) {
            // Xử lý lỗi nếu chuỗi thời gian không đúng định dạng
            throw new RuntimeException("Invalid date format, please use yyyy/MM/dd HH:mm:ss", e);
        }
    }
}

