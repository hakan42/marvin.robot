package com.assetvisor.marvin.robot.domain.ports;

import com.assetvisor.marvin.robot.domain.EnvironmentFunction;
import java.util.List;

public interface ForGettingEnvironmentFunctions {
    List<EnvironmentFunction<?, ?>> getEnvironmentFunctions();
}
