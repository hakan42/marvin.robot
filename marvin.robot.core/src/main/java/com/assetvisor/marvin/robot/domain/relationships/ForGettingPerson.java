package com.assetvisor.marvin.robot.domain.relationships;

import com.assetvisor.marvin.robot.domain.relationships.Person.ExternalId;

public interface ForGettingPerson {
    Person byId(String id);
    Person byEmail(String email);
    Person byExternalId(ExternalId externalId);
}
