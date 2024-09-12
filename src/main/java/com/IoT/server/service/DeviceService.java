package com.IoT.server.service;

import com.IoT.server.entity.Device;
import com.IoT.server.repository.DeviceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepo deviceRepo;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    public Device recordDeviceStatus(String name, boolean status) {
        Device device = new Device();

        device.setName(name);
        device.setStatus(status);
        device.setTimestamp(new Date()); // Ghi lại thời gian bật/tắt
        return deviceRepo.save(device);
    }

    public String formatActionTime(Date actionTime) {
        return dateFormat.format(actionTime); // Định dạng thời gian theo YYYY/MM/DD HH:MM:SS
    }

    public List<Device> getDeviceHistory() {
        return deviceRepo.findAll(); // Trả về toàn bộ lịch sử bật/tắt thiết bị
    }


}
