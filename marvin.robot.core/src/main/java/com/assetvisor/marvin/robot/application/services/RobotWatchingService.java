package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.RobotWatchesUseCase;
import com.assetvisor.marvin.robot.domain.brain.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import jakarta.annotation.Resource;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class RobotWatchingService implements RobotWatchesUseCase {

    Log LOG = LogFactory.getLog(getClass());

    @Resource
    private ForInvokingBrain forInvokingBrain;

    @Override
    public void observe(Observation observation) {
        String message = "Observed that item with the id " + observation.itemId() + " " + observation.description();
        LOG.info(message);
        forInvokingBrain.invoke(message, true);
    }
}
