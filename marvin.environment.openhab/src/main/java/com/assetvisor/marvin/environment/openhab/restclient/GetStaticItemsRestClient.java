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
public class GetStaticItemsRestClient {
    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;

    public List<Map<String, Object>> asMaps() {
        LOG.info("Getting static information for all items tagged 'Marvin'");
        try {
            return openhabRestClient.get()
                .uri("items?tags=Marvin&staticDataOnly=true")
                .retrieve()
                .body(LIST_OF_MAP_TYPE);
        } catch (HttpClientErrorException e) {
            LOG.error("Error: " + e.getMessage());
        }
        return List.of();
    }

    private static final ParameterizedTypeReference<List<Map<String, Object>>> LIST_OF_MAP_TYPE =
        new ParameterizedTypeReference<>() {};
}
