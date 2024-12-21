package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.SomethingWasSaidUseCase;
import com.assetvisor.marvin.robot.application.SomethingHappenedInTheEnvironmentUseCase;
import com.assetvisor.marvin.robot.application.SomethingWasTextedUseCase;
import com.assetvisor.marvin.robot.domain.brain.BrainResponder;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.communication.ConversationMessage;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingSpeechToText;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.SpeechMessage;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class InteractionService implements
    SomethingHappenedInTheEnvironmentUseCase,
    SomethingWasSaidUseCase,
    SomethingWasTextedUseCase {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForTexting forTexting;
    @Resource
    private ForConvertingTextToSpeech forConvertingTextToSpeech;
    @Resource
    private ForConvertingSpeechToText forConvertingSpeechToText;
    @Resource
    private ForCheckingIfAnybodyIsListening forCheckingIfAnybodyIsListening;
    @Resource
    private SpeechBuffer speechBuffer;

    @Override
    public void observe(Observation observation) {
        LOG.info(observation);
        forInvokingBrain.invoke(
            observation.toString(),
            true,
            new BrainResponder(
                forTexting,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ),
            ConversationMessage.DEFAULT_CONVERSATION_ID
        );
    }

    @Override
    public void read(TextMessage message, boolean echoToSender) {
        LOG.info(message);
        if(echoToSender) {
            forTexting.text(message, true);
        }
        forInvokingBrain.invoke(
            message.getContent(),
            true,
            new BrainResponder(
                forTexting,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ),
            message.conversationId()
        );
    }

    @Override
    public void listenTo(SpeechMessage speech) {
        String text = forConvertingSpeechToText.convert(speech.getAudio());
        LOG.info(text);
        forTexting.text(new TextMessage(speech.getSender(), speech.conversationId(), text), false);
        forInvokingBrain.invoke(
            text,
            true,
            new BrainResponder(
                forTexting,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ),
            speech.conversationId()
        );
    }

}
