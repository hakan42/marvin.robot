package com.assetvisor.marvin.environment.openhab.adapters;

import static java.util.stream.Stream.concat;

import com.assetvisor.marvin.environment.openhab.restclient.GetStaticRulesRestClient;
import com.assetvisor.marvin.environment.openhab.restclient.GetStaticItemsRestClient;
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
    private GetStaticItemsRestClient getStaticItemsRestClient;
    @Resource
    private GetStaticRulesRestClient getStaticRulesRestClient;

    @Override
    public List<EnvironmentDescription> getEnvironmentDescriptions() {
        return concat(
            getStaticItemsRestClient.asMaps().stream(),
            getStaticRulesRestClient.asMaps().stream()
        )
            .map(Object::toString)
            .map(EnvironmentDescription::new)
            .toList();
    }
}
