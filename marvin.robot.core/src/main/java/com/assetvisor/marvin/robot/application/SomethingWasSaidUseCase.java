package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.communication.SpeechMessage;

public interface SomethingWasSaidUseCase {
    void listenTo(SpeechMessage speech);
}
