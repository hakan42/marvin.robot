package com.assetvisor.marvin.interaction.web.adapters;

public record SseTextMessage(String sender, String content, boolean feedback) {}
