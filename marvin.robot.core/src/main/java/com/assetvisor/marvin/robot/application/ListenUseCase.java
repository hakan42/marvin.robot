package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.communication.AudioMessage;
import com.assetvisor.marvin.robot.domain.communication.TextMessage;

public interface ListenUseCase {
    void listenTo(TextMessage message);
    void listenTo(AudioMessage speech);
}
