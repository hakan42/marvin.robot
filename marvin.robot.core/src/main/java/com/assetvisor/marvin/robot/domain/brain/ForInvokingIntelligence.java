package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import java.util.List;

public interface ForInvokingIntelligence {
    void teach(
        List<EnvironmentDescription> environmentDescriptions
    );
    void wakeUp(
        RobotDescription robotDescription,
        List<Tool<?,?>> environmentFunctions
    );
    void invoke(String message, boolean reply, Brain brain, String conversationId);
}
