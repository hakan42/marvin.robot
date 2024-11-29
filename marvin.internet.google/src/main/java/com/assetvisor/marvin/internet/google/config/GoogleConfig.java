package com.assetvisor.marvin.internet.google.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.RequestHeadersSpec;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

@Configuration
public class GoogleConfig {

    @Value("${google.apikey}")
    private String apiKey;
    @Value("${google.cx}")
    private String customSearchEngineId;

    @Bean
    public RestClient restClient() {
        String baseUrl = "https://www.googleapis.com/customsearch/v1";

        return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
