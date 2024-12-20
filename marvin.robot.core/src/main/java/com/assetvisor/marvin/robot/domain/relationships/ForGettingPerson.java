package com.assetvisor.marvin.robot.domain.relationships;

public interface ForGettingPerson {
    Person byId(String id);
    Person byEmail(String email);

}
