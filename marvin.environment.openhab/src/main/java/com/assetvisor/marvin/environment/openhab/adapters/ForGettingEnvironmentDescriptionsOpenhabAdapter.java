package com.assetvisor.marvin.environment.openhab.adapters;

import com.assetvisor.marvin.environment.openhab.restclient.OpenhabStaticItemsService;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.ForGettingEnvironmentDescriptions;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("openhab")
public class ForGettingEnvironmentDescriptionsOpenhabAdapter implements ForGettingEnvironmentDescriptions {

    @Resource
    private OpenhabStaticItemsService openhabStaticItemsService;

    @Override
    public List<EnvironmentDescription> getEnvironmentDescriptions() {
        return openhabStaticItemsService.asMaps().stream()
            .map(Object::toString)
            .map(EnvironmentDescription::new)
            .toList();
    }
}
