package com.assetvisor.marvin.brain.springai.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAiBrainConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
}