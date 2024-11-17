package com.assetvisor.marvin.robot.domain.ports;

import com.assetvisor.marvin.robot.domain.EnvironmentDescription;
import java.util.List;

public interface ForGettingEnvironmentDescriptions {
    List<EnvironmentDescription> getEnvironmentDescriptions();
}
