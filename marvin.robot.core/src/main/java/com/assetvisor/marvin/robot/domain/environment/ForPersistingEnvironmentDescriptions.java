package com.assetvisor.marvin.robot.domain.environment;

import java.util.List;

public interface ForPersistingEnvironmentDescriptions {
    List<EnvironmentDescription> load();
    void save(EnvironmentDescription description);
}
