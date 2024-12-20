package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.ListenUseCase;
import com.assetvisor.marvin.robot.application.ObserveUseCase;
import com.assetvisor.marvin.robot.domain.brain.BrainResponder;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.communication.ConversationMessage;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingSpeechToText;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.AudioMessage;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class InteractionService implements ObserveUseCase, ListenUseCase {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForTexting forMessaging;
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
                forMessaging,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ),
            ConversationMessage.DEFAULT_CONVERSATION_ID
        );
    }

    @Override
    public void listenTo(TextMessage message) {
        LOG.info(message);
        forMessaging.text(message);
        forInvokingBrain.invoke(
            message.getContent(),
            true,
            new BrainResponder(
                forMessaging,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ),
            message.conversationId()
        );
    }

    @Override
    public void listenTo(AudioMessage speech) {
        String text = forConvertingSpeechToText.convert(speech.getAudio());
        LOG.info(text);
        forMessaging.text(new TextMessage(speech.getSender(), speech.conversationId(), text));
        forInvokingBrain.invoke(
            text,
            true,
            new BrainResponder(
                forMessaging,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ),
            speech.conversationId()
        );
    }

}
