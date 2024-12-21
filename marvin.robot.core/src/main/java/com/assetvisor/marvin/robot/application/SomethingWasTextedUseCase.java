package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.communication.TextMessage;

public interface SomethingWasTextedUseCase {
    void read(TextMessage message);
}
