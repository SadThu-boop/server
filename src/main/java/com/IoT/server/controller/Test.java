package com.IoT.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test {
    @GetMapping("/home")
    public String home() {
        return "index";
    }

    @GetMapping("/devicehistory")
    public String device(){
        return "devicehistory";
    }

    @GetMapping("/datasensor")
    public String sensor(){
        return "datasensor";
    }
}
