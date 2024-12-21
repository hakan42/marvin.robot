package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.SomethingHappenedInTheEnvironmentUseCase;
import com.assetvisor.marvin.robot.application.SomethingWasSaidUseCase;
import com.assetvisor.marvin.robot.application.SomethingWasTextedUseCase;
import com.assetvisor.marvin.robot.domain.brain.Brain;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingIntelligence;
import com.assetvisor.marvin.robot.domain.communication.ForCheckingIfAnybodyIsListening;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingSpeechToText;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForTexting;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.communication.SpeechMessage;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class BrainInteractionService implements
    SomethingHappenedInTheEnvironmentUseCase,
    SomethingWasSaidUseCase,
    SomethingWasTextedUseCase {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private ForInvokingIntelligence forInvokingIntelligence;
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
        brain().observe(observation);
    }

    @Override
    public void read(TextMessage message, boolean echoToSender) {
        brain().read(message, echoToSender);
    }

    @Override
    public void listenTo(SpeechMessage speech) {
        brain().listenTo(speech);
    }

    private Brain brain() {
        return new Brain(
            forTexting,
            forConvertingTextToSpeech,
            forConvertingSpeechToText,
            forCheckingIfAnybodyIsListening,
            speechBuffer,
            forInvokingIntelligence
        );
    }
}
