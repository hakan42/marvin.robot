package com.assetvisor.marvin.environment.openhab.adapters;

import com.assetvisor.marvin.environment.openhab.restclient.OpenhabAiItemsService;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.environment.ForPersistingEnvironmentDescriptions;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("openhab")
public class ForGettingEnvironmentDescriptionsOpenhabAdapter implements ForGettingEnvironmentDescriptions {

    @Resource
    private OpenhabAiItemsService openhabAiItemsService;
    @Resource
    private ForPersistingEnvironmentDescriptions forPersistingEnvironmentDescriptions;

    @Override
    public List<EnvironmentDescription> getEnvironmentDescriptions() {
        List<EnvironmentDescription> environmentDescriptions = forPersistingEnvironmentDescriptions.load();
        openhabAiItemsService.asMaps().forEach(item -> {
            environmentDescriptions.add(new EnvironmentDescription(item.toString()));
        });
        return environmentDescriptions;
    }
}
