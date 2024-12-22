package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingSpeechToText;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.communication.SpeechMessage;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Brain {

    private static final Log LOG = LogFactory.getLog(Brain.class);

    private final ForTexting forTexting;
    private final ForConvertingTextToSpeech forConvertingTextToSpeech;
    private final ForConvertingSpeechToText forConvertingSpeechToText;
    private final ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening;
    private final SpeechBuffer speechBuffer;
    private final ForInvokingIntelligence forInvokingIntelligence;

    public Brain(ForTexting forTexting,
        ForConvertingTextToSpeech forConvertingTextToSpeech,
        ForConvertingSpeechToText forConvertingSpeechToText,
        ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening,
        SpeechBuffer speechBuffer,
        ForInvokingIntelligence forInvokingIntelligence
    ) {
        this.forTexting = forTexting;
        this.forConvertingTextToSpeech = forConvertingTextToSpeech;
        this.forConvertingSpeechToText = forConvertingSpeechToText;
        this.forCheckingIfAnybodyIsListening = forCheckingIfAnybodyIsListening;
        this.speechBuffer = speechBuffer;
        this.forInvokingIntelligence = forInvokingIntelligence;
    }

    public void observe(Observation observation) {
        LOG.info(observation);
        forInvokingIntelligence.invoke(
            observation,
            this
        );
    }

    public void read(TextMessage message, boolean echoToSender) {
        LOG.info(message);
        if(echoToSender) {
            forTexting.text(message, true);
        }
        forInvokingIntelligence.invoke(
            message,
            this
        );
    }

    public void listenTo(SpeechMessage speech) {
        String text = forConvertingSpeechToText.convert(speech.getAudio());
        TextMessage textMessage = new TextMessage(
            speech.getSender(),
            speech.conversationId(),
            text
        );
        LOG.info(textMessage);
        forTexting.text(
            textMessage,
            true
        );
        forInvokingIntelligence.invoke(
            textMessage,
            this
        );
    }

    public void respond(String message, String conversationId) {
        message(message, conversationId);
        if(forCheckingIfAnybodyIsListening.isAnybodyListening()) {
            speak(message, conversationId);
        }
    }

    private void speak(String message, String conversationId) {
        byte[] speech = forConvertingTextToSpeech.convert(message);
        speechBuffer.add(new SpeechMessage("Marvin", conversationId, speech));
    }

    private void message(String message, String conversationId) {
        forTexting.text(new TextMessage("Marvin", conversationId, message), false);
    }
}
