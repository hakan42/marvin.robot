package com.assetvisor.marvin.robot.application;

public interface ListenUseCase {
    void listenTo(String message);
    void listenTo(byte[] audio);
}
