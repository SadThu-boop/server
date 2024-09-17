package com.IoT.server.controller.api.response;

import lombok.Data;

@Data
public class DeviceResponse {
    private String name;
    private String message;
    private boolean status;

    public DeviceResponse(String message, String name, boolean status) {
        this.name = name;
        this.message = message;
        this.status = status;
    }
}
