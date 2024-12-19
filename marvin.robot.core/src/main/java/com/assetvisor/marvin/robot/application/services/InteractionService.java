package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.ListenUseCase;
import com.assetvisor.marvin.robot.application.ObserveUseCase;
import com.assetvisor.marvin.robot.domain.brain.BrainResponder;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingSpeechToText;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForMessaging;
import com.assetvisor.marvin.robot.domain.communication.Message;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
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
    private ForMessaging forMessaging;
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
            ));
    }

    @Override
    public void listenTo(Message message) {
        LOG.info(message);
        forMessaging.message(message);
        forInvokingBrain.invoke(
            message.content(),
            true,
            new BrainResponder(
                forMessaging,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ));
    }

    @Override
    public void listenTo(String sender, byte[] audio) {
        String message = forConvertingSpeechToText.convert(audio);
        LOG.info(message);
        forMessaging.message(new Message(sender, message));
        forInvokingBrain.invoke(
            message,
            true,
            new BrainResponder(
                forMessaging,
                forConvertingTextToSpeech,
                forCheckingIfAnybodyIsListening,
                speechBuffer
            ));
    }

}
