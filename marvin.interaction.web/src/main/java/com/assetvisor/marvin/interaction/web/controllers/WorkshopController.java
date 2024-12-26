package com.assetvisor.marvin.interaction.web.controllers;

import com.assetvisor.marvin.robot.application.AddEnvironmentDescriptionUseCase;
import com.assetvisor.marvin.robot.application.DeleteEnvironmentDescriptionUseCase;
import com.assetvisor.marvin.robot.application.GetEnvironmentDescriptionsUseCase;
import com.assetvisor.marvin.robot.application.InitialiseUseCase;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import jakarta.annotation.Resource;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    @Resource
    private GetEnvironmentDescriptionsUseCase getEnvironmentDescriptionsUseCase;
    @Resource
    private DeleteEnvironmentDescriptionUseCase deleteEnvironmentDescriptionUseCase;


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

    @GetMapping("/environment")
    public List<EnvironmentDescription> getEnvironmentDescriptions(Principal principal) {
        return getEnvironmentDescriptionsUseCase.all();
    }

    @DeleteMapping("/environment")
    public void deleteEnvironmentDescription(
        Principal principal,
        @RequestParam(value = "id") String id
    ) {
        deleteEnvironmentDescriptionUseCase.delete(id);
    }
}
