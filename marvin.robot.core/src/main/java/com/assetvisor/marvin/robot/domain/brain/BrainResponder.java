package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForMessaging;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;

public class BrainResponder {
    private final ForMessaging forMessaging;
    private final ForConvertingTextToSpeech forConvertingTextToSpeech;
    private final SpeechBuffer speechBuffer;

    public BrainResponder(ForMessaging forMessaging, ForConvertingTextToSpeech forConvertingTextToSpeech,
        SpeechBuffer speechBuffer) {
        this.forMessaging = forMessaging;
        this.forConvertingTextToSpeech = forConvertingTextToSpeech;
        this.speechBuffer = speechBuffer;
    }

    public void respond(String message) {
        message(message);
        speak(message);
    }

    private void speak(String message) {
        byte[] speech = forConvertingTextToSpeech.convert(message);
        speechBuffer.add(speech);
    }

    private void message(String message) {
        forMessaging.message(message);
    }
}
