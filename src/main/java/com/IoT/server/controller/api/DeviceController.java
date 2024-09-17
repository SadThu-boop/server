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
    @PostMapping("/{deviceName}/{action}")
    public String turnOnDevice(@PathVariable String deviceName, @PathVariable String action) throws MqttException {
        String topic = "home/device/" + deviceName;
        boolean status = "on".equalsIgnoreCase(action);

        mqttService.publish(topic, action);
        deviceService.recordDeviceHistory(deviceName, status);
        return deviceName + " is turned " + action;
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
