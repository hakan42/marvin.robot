package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForMessaging;
import com.assetvisor.marvin.robot.domain.communication.Message;
import com.assetvisor.marvin.robot.domain.communication.Speech;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;

public class BrainResponder {
    private final ForMessaging forMessaging;
    private final ForConvertingTextToSpeech forConvertingTextToSpeech;
    private final ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening;
    private final SpeechBuffer speechBuffer;

    public BrainResponder(ForMessaging forMessaging, ForConvertingTextToSpeech forConvertingTextToSpeech,
        ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening,
        SpeechBuffer speechBuffer) {
        this.forMessaging = forMessaging;
        this.forConvertingTextToSpeech = forConvertingTextToSpeech;
        this.forCheckingIfAnybodyIsListening = forCheckingIfAnybodyIsListening;
        this.speechBuffer = speechBuffer;
    }

    public void respond(String message) {
        message(message);
        if(forCheckingIfAnybodyIsListening.isAnybodyListening()) {
            speak(message);
        }
    }

    private void speak(String message) {
        byte[] speech = forConvertingTextToSpeech.convert(message);
        speechBuffer.add(new Speech("Marvin", speech));
    }

    private void message(String message) {
        forMessaging.message(new Message("Marvin", message));
    }
}
