package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.domain.functions.Functions;
import com.assetvisor.marvin.robot.domain.ports.ForGettingEnvironmentDescriptions;
import com.assetvisor.marvin.robot.domain.ports.ForGettingEnvironmentFunctions;
import com.assetvisor.marvin.robot.domain.ports.ForInvokingBrain;
import com.assetvisor.marvin.robot.domain.ports.ForPersistingRobotDescription;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RobotLifecycleService {

    @Resource
    private ForInvokingBrain forInvokingBrain;
    @Resource
    private ForPersistingRobotDescription forPersistingRobotDescription;
    @Resource
    private ForGettingEnvironmentDescriptions forGettingEnvironmentDescriptions;
    @Resource
    private ForGettingEnvironmentFunctions forGettingEnvironmentFunctions;

    @PostConstruct
    public void born() {
        Functions functions = new Functions(forGettingEnvironmentFunctions);
        forInvokingBrain.initialise(
            forPersistingRobotDescription.read(),
            forGettingEnvironmentDescriptions.getEnvironmentDescriptions(),
            functions.all()
        );
        forInvokingBrain.invoke("You are born!", true);
    }
}
