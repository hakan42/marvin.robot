package com.assetvisor.marvin.interaction.web.controllers;

import com.assetvisor.marvin.robot.application.RobotListensUseCase;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HumanInteractionsController {

    @Resource
    private RobotListensUseCase robotListensUseCase;

    @PostMapping("/chat")
    public void chat(@RequestParam(value = "message") String message) {
        robotListensUseCase.listenTo(message);
    }
}
