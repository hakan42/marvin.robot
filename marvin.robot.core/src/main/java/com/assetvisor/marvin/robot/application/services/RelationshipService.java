package com.assetvisor.marvin.robot.application.services;

import static com.assetvisor.marvin.robot.domain.relationships.Person.Relationship.FRIEND;
import static com.assetvisor.marvin.robot.domain.relationships.Person.Relationship.STRANGER;

import com.assetvisor.marvin.robot.application.PersonEnteredUseCase;
import com.assetvisor.marvin.robot.application.PersonUco;
import com.assetvisor.marvin.robot.application.PersonWantsToEnterUseCase;
import com.assetvisor.marvin.robot.domain.relationships.ForPersistingPerson;
import com.assetvisor.marvin.robot.domain.relationships.Person;
import com.assetvisor.marvin.robot.domain.relationships.Person.IdType;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class RelationshipService implements PersonWantsToEnterUseCase, PersonEnteredUseCase {

    private static final Log LOG = LogFactory.getLog(RelationshipService.class);

    @Resource
    private ForPersistingPerson forPersistingPerson;

    @Override
    public EntryAttemptResponseUco attemptEntry(PersonUco personUco) {
        Person person = forPersistingPerson.byEmail(personUco.getEmail());
        if(person == null) {
            //no idea who this is, but remember him as stranger
            forPersistingPerson.addPerson(
                new Person(
                    UUID.randomUUID().toString(),
                    personUco.getName(),
                    personUco.getEmail(),
                    null,
                    STRANGER,
                    List.of(
                        new Person.PersonId(
                            IdType.valueOf(personUco.getId().idType().name()),
                            personUco.getId().value()
                        )
                    )
                ));
            return new EntryAttemptResponseUco(false);
        }
        if(!person.hasExternalId(personUco.getId())) {
            //this person is known, but with different id
            person.addExternalId(personUco.getId(), forPersistingPerson);
        }
        if(person.relationship() == FRIEND) {
            //welcome back
            return new EntryAttemptResponseUco(true);
        }
        //not a friend, but we know him
        return new EntryAttemptResponseUco(false);
    }

    @Override
    public void personEntered(Person.PersonId personId) {
        Person person = forPersistingPerson.byExternalId(personId);
        if(person == null) {
            LOG.warn("Unknown person entered: " + personId);
            return;
        }
        LOG.info("Person entered: " + person);
    }
}
