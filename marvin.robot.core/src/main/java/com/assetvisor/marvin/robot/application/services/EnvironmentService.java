package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.AddEnvironmentDescriptionUseCase;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentService implements AddEnvironmentDescriptionUseCase {

    @Resource
    private ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;

    @Override
    public void add(String description) {
        forPersistingEnvironmentDescriptions.save(
            new EnvironmentDescription(description)
        );
    }
}
