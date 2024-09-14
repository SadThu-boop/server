package com.IoT.server.controller.api;

import java.util.List;

import com.IoT.server.controller.api.request.DeviceRequest;
import com.IoT.server.service.MQTTService;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.IoT.server.entity.Device;
import com.IoT.server.service.DeviceService;

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
                return null; // hoặc ném ngoại lệ nếu tên không hợp lệ
        }
    }


    @GetMapping("/deviceHistory")
    public List<Device> getDeviceHistory() {
        return deviceService.getDeviceHistory();
    }

    @PostMapping("/{deviceName}/{action}")
    public String turnOnDevice(@PathVariable String deviceName, @PathVariable String action) {
        String topic = "home/device/" + deviceName;
        boolean status = "on".equalsIgnoreCase(action); // Kiểm tra nếu action là "on"

        try{
            mqttService.publish(topic,action);

            deviceService.recordDeviceHistory(deviceName,status);
            return deviceName + " is turned " + action;

        }catch(MqttException e) {
            e.printStackTrace();
            return "Error turning " + action + " " + deviceName;
        }
    }
    @PostMapping("/device/control")
    public ResponseEntity<String> controlDevice(@RequestBody DeviceRequest deviceRequest) {
        String deviceName = mapDeviceName(deviceRequest.getName()); // ánh xạ tên thiết bị
        if (deviceName == null) {
            return ResponseEntity.badRequest().body("Invalid device name");
        }

        String action = deviceRequest.getAction();
        boolean status = "on".equalsIgnoreCase(action);

        try {
            String topic = "home/device/" + deviceName.toLowerCase(); // topic với tên đầy đủ
            mqttService.publish(topic, action); // Gửi lệnh đến MQTT
            deviceService.recordDeviceHistory(deviceName, status); // Ghi lại lịch sử thiết bị
            return ResponseEntity.ok(deviceName + " is turned " + action);
        } catch (MqttException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error turning " + action + " " + deviceName);
        }
    }
}
