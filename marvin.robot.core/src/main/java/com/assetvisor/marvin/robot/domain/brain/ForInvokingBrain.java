package com.assetvisor.marvin.robot.domain.brain;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.environment.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.environment.Observation;
import com.assetvisor.marvin.robot.domain.jobdescription.RobotDescription;
import java.util.List;

public interface ForInvokingBrain {
    void initialise(
        RobotDescription robotDescription,
        List<EnvironmentDescription> environmentDescriptions,
        List<EnvironmentFunction<?,?>> environmentFunctions
    );
    void invoke(Observation observation, boolean reply);
    void invoke(String message, boolean reply);
}