package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.RobotListensUseCase;
import com.assetvisor.marvin.robot.domain.ports.ForInvokingBrain;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RobotListeningService implements RobotListensUseCase {

    @Resource
    private ForInvokingBrain brain;

    @Override
    public void listenTo(String message) {
        brain.invoke(message, true);
    }

}
