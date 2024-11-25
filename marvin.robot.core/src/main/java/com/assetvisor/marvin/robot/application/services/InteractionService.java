package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.RobotListensUseCase;
import com.assetvisor.marvin.robot.application.RobotWatchesUseCase;
import com.assetvisor.marvin.robot.domain.brain.BrainResponder;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.communication.ForConvertingTextToSpeech;
import com.assetvisor.marvin.robot.domain.communication.ForMessaging;
import com.assetvisor.marvin.robot.domain.communication.SpeechBuffer;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class InteractionService implements RobotWatchesUseCase, RobotListensUseCase {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForMessaging forMessaging;
    @Resource
    private ForConvertingTextToSpeech forConvertingTextToSpeech;
    @Resource
    private SpeechBuffer speechBuffer;

    @Override
    public void observe(Observation observation) {
        LOG.info(observation);
        forInvokingBrain.invoke(
            observation,
            true,
            new BrainResponder(
                forMessaging,
                forConvertingTextToSpeech,
                speechBuffer
            ));
    }

    @Override
    public void listenTo(String message) {
        LOG.info(message);
        forInvokingBrain.invoke(
            message,
            true,
            new BrainResponder(
                forMessaging,
                forConvertingTextToSpeech,
                speechBuffer
            ));
    }
}