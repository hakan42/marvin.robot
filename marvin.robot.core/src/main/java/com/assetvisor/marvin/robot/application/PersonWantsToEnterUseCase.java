package com.assetvisor.marvin.robot.application;

public interface PersonWantsToEnterUseCase {

    EntryAttemptResponseUco attemptEntry(PersonUco personUco);

    record EntryAttemptResponseUco(boolean ok) {}
}
