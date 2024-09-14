//package com.IoT.server.service;
//
//import org.eclipse.paho.client.mqttv3.*;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Mosquitto {
//    private MqttClient client;
//
//    public Mosquitto() throws MqttException {
//        String broker = "tcp://localhost:1883";  // Địa chỉ MQTT broker của bạn
//        String clientId = MqttClient.generateClientId();
//        client = new MqttClient(broker, clientId);
//
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setCleanSession(true);
//        client.connect(options);
//
//        client.setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) {
//                System.out.println("Connection lost: " + cause.getMessage());
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                System.out.println("Message received: " + message);
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//                System.out.println("Delivery complete: " + token);
//            }
//        });
//    }
//
//    public void publish(String topic, String payload) throws MqttException {
//        MqttMessage message = new MqttMessage(payload.getBytes());
//        message.setQos(1);
//        client.publish(topic, message);
//    }
//}
