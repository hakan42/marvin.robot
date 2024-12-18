package com.assetvisor.marvin.interaction.web.controllers;

import com.assetvisor.marvin.robot.application.InitialiseUseCase;
import jakarta.annotation.Resource;
import java.security.Principal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class WorkshopController {

    @Resource
    private InitialiseUseCase initialiseUseCase;

    @PostMapping("/initialise")
    public void initialise(Principal principal) {
        initialiseUseCase.initialise();
    }
}
