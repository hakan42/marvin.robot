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
public class OpenhabStaticItemsService {
    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient restClient;

    public List<Map<String, Object>> asMaps() {
        LOG.info("Getting all items tagged Marvin");
        try {
            List<Map<String, Object>> body = restClient.get()
                .uri("items?tags=Marvin&staticDataOnly=true")
                .retrieve()
                .body(List.class);
            return body;
        } catch (HttpClientErrorException e) {
            LOG.error("Error: " + e.getMessage());
        }
        return List.of();
    }
}
