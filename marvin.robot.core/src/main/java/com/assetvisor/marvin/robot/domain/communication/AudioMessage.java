package com.assetvisor.marvin.robot.domain.communication;

import java.util.Arrays;

public final class AudioMessage extends Message {

    private final byte[] audio;

    public AudioMessage(String sender, String conversationId, byte[] audio) {
        super(sender, conversationId);
        this.audio = audio;
    }

    public byte[] getAudio() {
        return audio;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AudioMessage that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return Arrays.equals(audio, that.audio);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(audio);
        return result;
    }
}