package com.IoT.server.controller.api;

import com.IoT.server.entity.Device;
import com.IoT.server.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/updateDeviceStatus")
    public void updateDeviceStatus(@RequestParam String name, @RequestParam boolean status) {
        Device device = deviceService.recordDeviceStatus(name, status);
        String formattedTime = deviceService.formatActionTime(device.getTimestamp());
    }

    @GetMapping("/deviceHistory")
    public List<Device> getDeviceHistory() {
        return deviceService.getDeviceHistory();
    }

}
