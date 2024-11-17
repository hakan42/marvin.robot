package com.assetvisor.marvin.brain.springai.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SpringAiConfig {
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}
