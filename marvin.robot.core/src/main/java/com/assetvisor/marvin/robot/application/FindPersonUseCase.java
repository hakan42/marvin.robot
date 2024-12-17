package com.assetvisor.marvin.robot.application;

import com.assetvisor.marvin.robot.domain.relationships.Person.Relationship;

public interface FindPersonUseCase {
    PersonUco findPersonByEmail(String email);
    record PersonUco(String email, Relationship relationship) {}
}
