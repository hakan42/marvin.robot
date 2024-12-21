package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.environment.Observation;

public interface SomethingHappenedInTheEnvironmentUseCase {
    void observe(Observation observation);
}
