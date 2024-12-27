package com.assetvisor.marvin.robot.domain.relationships;

public interface ForPersistingPerson {
    Person byId(String id);
    Person byEmail(String email);
    Person byExternalId(Person.PersonId externalId);
    void addPerson(Person person);
    void addExternalId(Person person, Person.PersonId externalId);
}
