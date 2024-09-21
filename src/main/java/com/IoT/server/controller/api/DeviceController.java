package com.IoT.server.controller.api;

import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.IoT.server.controller.api.request.DeviceRequest;
import com.IoT.server.controller.api.response.DeviceResponse;
import com.IoT.server.entity.Device;
import com.IoT.server.service.DeviceService;
import com.IoT.server.service.MQTTService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MQTTService mqttService;

    private String mapDeviceName(String name) {
        switch (name.toLowerCase()) {
            case "air conditioner":
                return "ac";
            case "fan":
                return "fan";
            case "light bulb":
                return "light";
            default:
                return null; // Tên không hợp lệ
        }
    }

    @Operation(
            summary = "Lấy danh sách thiết bị",
            description = "Trả về danh sách các thiết bị đã được ghi lại trong cơ sở dữ liệu")
    @GetMapping("/deviceHistory")
    public List<Device> getDeviceHistory() {
        return deviceService.getDeviceHistory();
    }

    @Operation(summary = "Bật/Tắt thiết bị", description = "Bật hoặc tắt thiết bị theo tên và hành động được cung cấp")
    @PutMapping("/{deviceName}/{action}")
    public ResponseEntity<String> turnOnDevice(@PathVariable String deviceName, @PathVariable String action) throws MqttException {
        // Validate deviceName parameter
        if (deviceName == null || deviceName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Device name cannot be empty.");
        }

        // Add any further checks on deviceName validity if necessary
        if (!isValidDeviceName(deviceName)) {
            return ResponseEntity.badRequest().body("Invalid device name provided.");
        }

        // Validate action parameter
        if (!"on".equalsIgnoreCase(action) && !"off".equalsIgnoreCase(action)) {
            return ResponseEntity.badRequest().body("Invalid action: must be 'on' or 'off'.");
        }

        String topic = "home/device/" + deviceName;
        boolean status = "on".equalsIgnoreCase(action);

        // Publish to MQTT and record device history
        mqttService.publish(topic, action);
        deviceService.recordDeviceHistory(deviceName, status);

        return ResponseEntity.ok(deviceName + " is turned " + action);
    }

    // Helper method to validate device name format or check against allowed device names
    private boolean isValidDeviceName(String deviceName) {
        // Add logic to validate device names, e.g., matching a regex pattern, or checking against a list of valid device names.
        return deviceName.matches("^[a-zA-Z0-9_-]+$"); // Example: only letters, numbers, hyphens, and underscores allowed
    }



    @Operation(
            summary = "Điều khiển thiết bị thông qua JSON",
            description = "Nhận yêu cầu điều khiển thiết bị từ JSON và gửi lệnh qua MQTT.")
    @PostMapping("/device/control")
    public ResponseEntity<DeviceResponse> controlDevice(@RequestBody DeviceRequest deviceRequest) throws MqttException {
        String deviceName = mapDeviceName(deviceRequest.getName());

        if (deviceName == null) {
            throw new IllegalArgumentException("Invalid device name: " + deviceRequest.getName());
        }

        String action = deviceRequest.getAction();
        boolean status = "on".equalsIgnoreCase(action);

        String topic = "home/device/" + deviceName.toLowerCase();
        mqttService.publish(topic, action);
        deviceService.recordDeviceHistory(deviceName, status);

        return ResponseEntity.ok(new DeviceResponse(deviceName + " is turned " + action, deviceName, status));
    }
}
