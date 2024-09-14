package com.IoT.server.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    public void recordDeviceHistory(String deviceName,boolean status) {
        Device device = new Device();
        device.setName(deviceName);
        device.setStatus(status);
        device.setTimestamp(LocalDateTime.now());
        deviceRepo.save(device);
    }

    public List<Device> getDeviceHistory() {
        return deviceRepo.findAll(); // Trả về toàn bộ lịch sử bật/tắt thiết bị
    }
}
