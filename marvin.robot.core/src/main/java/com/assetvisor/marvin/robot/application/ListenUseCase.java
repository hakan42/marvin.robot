package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.communication.Message;

public interface ListenUseCase {
    void listenTo(Message message);
    void listenTo(String sender, byte[] audio);
}
