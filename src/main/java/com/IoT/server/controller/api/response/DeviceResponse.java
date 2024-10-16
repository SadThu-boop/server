package com.IoT.server.controller.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponse {
    private int id;
    private String name;
    private String message;
    private boolean status;
    private String timestamp;


}
