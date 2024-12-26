package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.environment.EnvironmentDescription;
import java.util.List;

public interface GetEnvironmentDescriptionsUseCase {
    List<EnvironmentDescription> all();

}
