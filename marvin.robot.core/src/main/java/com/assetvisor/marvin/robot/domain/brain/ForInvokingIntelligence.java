package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.communication.ConversationMessage;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import com.assetvisor.marvin.robot.domain.tools.Tool;
import java.util.List;

public interface ForInvokingIntelligence {
    void teach(
        List<EnvironmentDescription> environmentDescriptions
    );
    void wakeUp(
        RobotDescription robotDescription,
        List<Tool<?,?>> environmentFunctions
    );
    void invoke(ConversationMessage message, Brain brain);
    void invoke(Observation observation, Brain brain);
}
