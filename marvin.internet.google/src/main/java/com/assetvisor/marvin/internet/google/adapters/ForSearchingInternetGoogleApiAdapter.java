package com.assetvisor.marvin.internet.google.adapters;

import com.assetvisor.marvin.equipment.internet.ForSearchingInternet;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class ForSearchingInternetGoogleApiAdapter implements ForSearchingInternet {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private RestClient restClient;
    @Resource
    private Environment env;

    @Override
    public String search(String query) {
        LOG.info(query);
        try {
            String body = restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .queryParam("key", env.getProperty("google.apikey"))
                    .queryParam("cx", env.getProperty("google.cx"))
                    .queryParam("q", query)
                    .build())
                .retrieve()
                .body(String.class);
            return body;
        } catch (HttpClientErrorException e) {
            return e.getMessage();
        }

    }
}
