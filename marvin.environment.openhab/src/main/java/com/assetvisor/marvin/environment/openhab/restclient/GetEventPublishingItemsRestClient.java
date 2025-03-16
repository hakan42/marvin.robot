package com.assetvisor.marvin.environment.openhab.restclient;

import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@Profile("environment-openhab")
public class GetEventPublishingItemsRestClient {
    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;

    public List<String> asIds() {
        LOG.info("Getting all item ids tagged 'Marvin' and 'Events'");
        try {
            return openhabRestClient.get()
                .uri("items?tags=Marvin,Events&fields=name")
                .retrieve()
                .body(LIST_OF_MAP_TYPE)
                .stream()
                .map(item -> item.get("name"))
                .toList();
        } catch (HttpClientErrorException e) {
            LOG.error("Error: " + e.getMessage());
        }
        return List.of();
    }

    private static final ParameterizedTypeReference<List<Map<String, String>>> LIST_OF_MAP_TYPE =
        new ParameterizedTypeReference<>() {};
}
