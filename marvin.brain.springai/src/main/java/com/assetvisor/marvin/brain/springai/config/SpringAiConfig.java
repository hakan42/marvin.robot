package com.assetvisor.marvin.brain.springai.config;

import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SpringAiConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    @Bean
    public OpenAiAudioSpeechModel openAiAudioSpeechModel() {
        OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(openAiApiKey);
        return new OpenAiAudioSpeechModel(openAiAudioApi);
    }
}
