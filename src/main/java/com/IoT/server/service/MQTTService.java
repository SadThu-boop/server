package com.IoT.server.service;

import com.IoT.server.entity.Device;
import com.IoT.server.entity.SensorData;
import com.IoT.server.repository.DeviceRepo;
import com.IoT.server.repository.SensorRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQTTService {

    @Autowired
    private SensorRepo sensorRepo;

    @Autowired
    private DeviceRepo deviceRepo;

    private final String broker = "tcp://localhost:1883";  // Địa chỉ MQTT broker
    private final String clientId = "SpringClient";
    private final String sensorTopic = "home/sensor/dht11";      // Topic để nhận dữ liệu từ cảm biến
    private final String deviceTopic = "home/device/control";    // Topic để điều khiển thiết bị (đèn LED)
    private MqttClient mqttClient;                               // Khởi tạo MqttClient ở cấp độ class để dùng chung

    public MQTTService() throws MqttException {
        mqttClient = new MqttClient(broker, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        mqttClient.connect(options);

        // Đăng ký lắng nghe topic để nhận dữ liệu từ ESP8266 (sensor)
        mqttClient.subscribe(sensorTopic, (topic, message) -> {
            String payload = new String(message.getPayload());
            System.out.println("Received sensor data: " + payload);

            // Chuyển đổi dữ liệu JSON và lưu vào cơ sở dữ liệu
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                SensorData sensorData = objectMapper.readValue(payload, SensorData.class);
                sensorRepo.save(sensorData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Đăng ký lắng nghe topic để nhận lệnh điều khiển thiết bị
        mqttClient.subscribe(deviceTopic, (topic, message) -> {
            String payload = new String(message.getPayload());
            System.out.println("Received control message: " + payload);

            // Xử lý dữ liệu điều khiển từ JSON và thực hiện điều khiển thiết bị
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Device device = objectMapper.readValue(payload, Device.class);
                deviceRepo.save(device);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Phương thức để điều khiển đèn LED hoặc các thiết bị khác
    public void controlDevice(String deviceName, boolean status) throws MqttException {
        Device device = new Device();
        device.setName(deviceName);
        device.setStatus(status);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String payload = objectMapper.writeValueAsString(device);
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            mqttClient.publish(deviceTopic, message);  // Gửi dữ liệu điều khiển đến thiết bị
            System.out.println("Published control message: " + payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm publish để gửi lệnh điều khiển các thiết bị qua MQTT
    public void publish(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);  // QoS 1: Đảm bảo rằng tin nhắn được gửi ít nhất một lần
        mqttClient.publish(topic, message);  // Gửi tin nhắn đến topic được chỉ định
        System.out.println("Published message to topic " + topic + ": " + payload);
    }
}
