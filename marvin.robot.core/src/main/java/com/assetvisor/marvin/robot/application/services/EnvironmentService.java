package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.AddEnvironmentDescriptionUseCase;
import com.assetvisor.marvin.robot.application.GetEnvironmentDescriptionsUseCase;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentService implements AddEnvironmentDescriptionUseCase, GetEnvironmentDescriptionsUseCase {

    @Resource
    private ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;

    @Override
    public void add(String description) {
        forPersistingEnvironmentDescriptions.save(
            new EnvironmentDescription(
                UUID.randomUUID().toString(),
                description
            )
        );
    }

    @Override
    public List<EnvironmentDescription> all() {
        return forPersistingEnvironmentDescriptions.all();
    }
}
