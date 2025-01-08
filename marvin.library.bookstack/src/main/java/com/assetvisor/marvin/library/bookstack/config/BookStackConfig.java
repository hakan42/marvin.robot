package com.assetvisor.marvin.library.bookstack.config;

import static com.assetvisor.marvin.toolkit.ProfileChecker.LIBRARY_BOOKSTACK;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@Configuration
@Profile(LIBRARY_BOOKSTACK)
public class BookStackConfig {

    @Value("${bookstack.tokenid}")
    private String tokenId;
    @Value("${bookstack.tokensecret}")
    private String tokenSecret;
    @Value("${bookstack.url}")
    private String url;

    @Bean
    public RestClient bookStackRestClient() {
        String baseUrl = url + "/api/";

        return RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeaders(headers ->
                headers.add("Authorization", "Token " + tokenId + ":" + tokenSecret)
            )
            .build();
    }
}
