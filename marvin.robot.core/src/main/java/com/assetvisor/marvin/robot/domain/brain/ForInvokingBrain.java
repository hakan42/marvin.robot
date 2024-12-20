package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import java.util.List;

public interface ForInvokingBrain {
    void teach(
        List<EnvironmentDescription> environmentDescriptions
    );
    void wakeUp(
        RobotDescription robotDescription,
        List<EnvironmentFunction<?,?>> environmentFunctions
    );
    void invoke(String message, boolean reply, BrainResponder responder, String conversationId);
}
