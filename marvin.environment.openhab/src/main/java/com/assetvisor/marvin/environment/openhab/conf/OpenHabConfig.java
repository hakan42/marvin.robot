package com.assetvisor.marvin.environment.openhab.conf;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("environment-openhab")
public class OpenHabConfig {

    @Value("${openhab.username}")
    private String userName;
    @Value("${openhab.password}")
    private String password;
    @Value("${openhab.url}")
    private String url;

    @Bean
    public RestClient openhabRestClient() {
         return RestClient.builder()
            .baseUrl(url + "/rest/")
            .defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()))
            .build();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
            .baseUrl(url + "/rest/")
            .defaultHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + password).getBytes()))
            .build();
    }
}
