package com.assetvisor.marvin.interaction.web;

import org.springframework.stereotype.Component;

@Component
public class AudioBuffer {

    private byte[] audio;

    public void set(byte[] audio) {
        this.audio = audio;
    }

    public byte[] get() {
        return audio;
    }
}
