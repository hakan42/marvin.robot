package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.environment.Observation;

public interface ObserveUseCase {
    void observe(Observation observation);
}
