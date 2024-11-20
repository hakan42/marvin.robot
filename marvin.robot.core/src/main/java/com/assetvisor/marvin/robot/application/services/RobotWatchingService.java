package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.RobotWatchesUseCase;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import jakarta.annotation.Resource;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class RobotWatchingService implements RobotWatchesUseCase {

    @Resource
    private ForInvokingBrain forInvokingBrain;

    @Override
    public void observe(Map<?, ?> event) {
        forInvokingBrain.invoke("Received event: " + event, true);
    }
}
