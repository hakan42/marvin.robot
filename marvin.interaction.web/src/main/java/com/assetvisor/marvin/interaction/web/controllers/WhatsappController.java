package com.assetvisor.marvin.interaction.web.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class WhatsappController {

    @GetMapping("/whatsapp")
    public String hello() {
        return "Hello";
    }
}
