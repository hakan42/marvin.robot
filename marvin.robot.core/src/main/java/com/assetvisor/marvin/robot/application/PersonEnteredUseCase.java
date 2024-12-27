package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.relationships.Person;

public interface PersonEnteredUseCase {
    void personEntered(Person.PersonId personId);
}
