package com.assetvisor.marvin.robot.domain.environment;

import java.util.List;

public interface ForPersistingEnvironmentDescriptions {
    List<EnvironmentDescription> all();
    void save(EnvironmentDescription description);
}
