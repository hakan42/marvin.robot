package com.assetvisor.marvin.environment.openhab.restclient;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@Profile("openhab")
public class GetEventPublishingItemsRestClient {
    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;

    public List<String> asIds() {
        LOG.info("Getting all item ids tagged 'Marvin' and 'Events'");
        try {
            List<Map> body = openhabRestClient.get()
                .uri("items?tags=Marvin,Events&fields=name")
                .retrieve()
                .body(List.class);
            return body.stream().map(item -> (String) item.get("name")).toList();
        } catch (HttpClientErrorException e) {
            LOG.error("Error: " + e.getMessage());
        }
        return List.of();
    }

}
