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
public class GetStaticRulesRestClient {
    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient openhabRestClient;

    public List<Map<String, Object>> asMaps() {
        LOG.info("Getting static information for all rules tagged 'Marvin'");
        try {
            List<Map<String, Object>> body = openhabRestClient.get()
                .uri("rules?tags=Marvin&staticDataOnly=true")
                .retrieve()
                .body(LIST_OF_MAP_TYPE);
            List<Map<String, Object>> maps = filterByTagMarvin(body);
            appendRuleInfo(maps);
            return maps;
        } catch (HttpClientErrorException e) {
            LOG.error("Error: " + e.getMessage());
        }
        return List.of();
    }

    private void appendRuleInfo(List<Map<String, Object>> body) {
        body.forEach(rule -> {
            rule.put("type", "rule");
        });
    }

    private List<Map<String, Object>> filterByTagMarvin(List<Map<String, Object>> body) {
        return body.stream()
            .filter(rule -> rule.get("tags").toString().contains("Marvin"))
            .toList();
    }

    private static final ParameterizedTypeReference<List<Map<String, Object>>> LIST_OF_MAP_TYPE =
        new ParameterizedTypeReference<>() {};
}
