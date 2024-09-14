//package com.IoT.server.controller.api.request;
//
//import com.IoT.server.entity.Device;
//import com.IoT.server.repository.DeviceRepo;
//import com.IoT.server.service.MQTTService;
//import com.IoT.server.service.Mosquitto;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//
//@RestController
//@RequestMapping("/api/device")
//public class Controller {
//    @Autowired
//    private MQTTService mqttService;
//
//    @Autowired
//    private DeviceRepo deviceRepository;
//
//    // Bật thiết bị
//    @PostMapping("/{deviceName}/on")
//    public String turnOnDevice(@PathVariable String deviceName) {
//        String topic = "home/device/" + deviceName;
//        try {
//            mqttService.publish(topic, "on");
//
//            // Lưu trạng thái bật vào cơ sở dữ liệu
//            Device device = new Device();
//            device.setName(deviceName);
//            device.setStatus(true);  // true = on
//            device.setTimestamp(LocalDateTime.now());
//            deviceRepository.save(device);
//
//            return deviceName + " is turned on";
//        } catch (MqttException e) {
//            e.printStackTrace();
//            return "Error turning on " + deviceName;
//        }
//    }
//
//    // Tắt thiết bị
//    @PostMapping("/{deviceName}/off")
//    public String turnOffDevice(@PathVariable String deviceName) {
//        String topic = "home/device/" + deviceName;
//        try {
//            mqttService.publish(topic, "off");
//
//            // Lưu trạng thái tắt vào cơ sở dữ liệu
//            Device device = new Device();
//            device.setName(deviceName);
//            device.setStatus(false);  // false = off
//            device.setTimestamp(LocalDateTime.now());
//            deviceRepository.save(device);
//
//            return deviceName + " is turned off";
//        } catch (MqttException e) {
//            e.printStackTrace();
//            return "Error turning off " + deviceName;
//        }
//    }
//}