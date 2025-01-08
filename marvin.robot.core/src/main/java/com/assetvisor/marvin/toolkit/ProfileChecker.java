package com.assetvisor.marvin.toolkit;

import jakarta.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ProfileChecker {

    @Resource
    private Environment environment;

    public boolean isProfileActive(String profile) {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String activeProfile : activeProfiles) {
            if (activeProfile.equals(profile)) {
                return true;
            }
        }
        return false;
    }

    public static final String ENVIRONMENT_OPENHAB = "environment-openhab";
    public static final String LIBRARY_BOOKSTACK = "library-bookstack";
    public static final String CHAT_OPENAI = "chat-openai";
}
