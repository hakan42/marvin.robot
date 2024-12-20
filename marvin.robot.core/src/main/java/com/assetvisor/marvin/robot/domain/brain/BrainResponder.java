package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.AudioMessage;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;

public class BrainResponder {
    private final ForTexting forMessaging;
    private final ForConvertingTextToSpeech forConvertingTextToSpeech;
    private final ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening;
    private final SpeechBuffer speechBuffer;

    public BrainResponder(ForTexting forMessaging, ForConvertingTextToSpeech forConvertingTextToSpeech,
        ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening,
        SpeechBuffer speechBuffer) {
        this.forMessaging = forMessaging;
        this.forConvertingTextToSpeech = forConvertingTextToSpeech;
        this.forCheckingIfAnybodyIsListening = forCheckingIfAnybodyIsListening;
        this.speechBuffer = speechBuffer;
    }

    public void respond(String message, String conversationId) {
        message(message, conversationId);
        if(forCheckingIfAnybodyIsListening.isAnybodyListening()) {
            speak(message, conversationId);
        }
    }

    private void speak(String message, String conversationId) {
        byte[] speech = forConvertingTextToSpeech.convert(message);
        speechBuffer.add(new AudioMessage("Marvin", conversationId, speech));
    }

    private void message(String message, String conversationId) {
        forMessaging.text(new TextMessage("Marvin", conversationId, message));
    }
}
