package com.assetvisor.marvin.interaction.web.controllers;

import com.assetvisor.marvin.robot.application.AddEnvironmentDescriptionUseCase;
import com.assetvisor.marvin.robot.application.InitialiseUseCase;
import jakarta.annotation.Resource;
import java.security.Principal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class WorkshopController {

    @Resource
    private InitialiseUseCase initialiseUseCase;
    @Resource
    private AddEnvironmentDescriptionUseCase addEnvironmentDescriptionUseCase;


    @PostMapping("/initialise")
    public void initialise(Principal principal) {
        initialiseUseCase.initialise();
    }

    @PostMapping("/environment")
    public void addEnvironmentDescription(
        Principal principal,
        @RequestParam(value = "description") String description
    ) {
        addEnvironmentDescriptionUseCase.add(description);
    }

}
