package com.assetvisor.marvin.internet.google.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GoogleConfig {

    @Bean
    public RestClient googleRestClient() {
        String baseUrl = "https://www.googleapis.com/customsearch/v1";

        return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
}
