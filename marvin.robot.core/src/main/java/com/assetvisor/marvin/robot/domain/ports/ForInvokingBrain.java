package com.assetvisor.marvin.robot.domain.ports;

import com.assetvisor.marvin.robot.domain.EnvironmentDescription;
import com.assetvisor.marvin.robot.domain.EnvironmentFunction;
import com.assetvisor.marvin.robot.domain.RobotDescription;
import java.util.List;

public interface ForInvokingBrain {
    void initialise(
        RobotDescription robotDescription,
        List<EnvironmentDescription> environmentDescriptions,
        List<EnvironmentFunction<?,?>> environmentFunctions
    );
    void invoke(String message, boolean reply);
}
