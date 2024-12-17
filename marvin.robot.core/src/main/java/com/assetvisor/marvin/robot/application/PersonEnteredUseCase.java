package com.assetvisor.marvin.robot.application;

public interface PersonEnteredUseCase {

    void personEntered(PersonUco personUco);

    record PersonUco(String personName, String email) {
    }
}
