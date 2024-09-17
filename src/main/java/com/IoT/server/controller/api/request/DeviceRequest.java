package com.IoT.server.controller.api.request;

import lombok.Data;

@Data
public class DeviceRequest {
    private String name;
    private String action;
}
