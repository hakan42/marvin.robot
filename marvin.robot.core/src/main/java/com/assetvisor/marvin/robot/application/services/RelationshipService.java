package com.assetvisor.marvin.robot.application.services;

import static com.assetvisor.marvin.robot.domain.relationships.Person.Relationship.FRIEND;
import static com.assetvisor.marvin.robot.domain.relationships.Person.Relationship.STRANGER;

import com.assetvisor.marvin.robot.application.PersonEnteredUseCase;
import com.assetvisor.marvin.robot.application.PersonUco;
import com.assetvisor.marvin.robot.application.PersonUco.PersonId;
import com.assetvisor.marvin.robot.application.PersonWantsToEnterUseCase;
import com.assetvisor.marvin.robot.domain.relationships.ForAddingPerson;
import com.assetvisor.marvin.robot.domain.relationships.ForGettingPerson;
import com.assetvisor.marvin.robot.domain.relationships.Person;
import jakarta.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class RelationshipService implements PersonWantsToEnterUseCase, PersonEnteredUseCase {

    private static final Log LOG = LogFactory.getLog(RelationshipService.class);

    @Resource
    private ForGettingPerson forGettingPerson;
    @Resource
    private ForAddingPerson forAddingPerson;

    @Override
    public EntryAttemptResponseUco attemptEntry(PersonUco personUco) {
        Person person = forGettingPerson.getPerson(personUco.getEmail());
        if(person == null) {
            //no idea who this is, but remember him as stranger
            forAddingPerson.addPerson(new Person(personUco.getName(), personUco.getEmail(), STRANGER));
            return new EntryAttemptResponseUco(false);
        }
        if(person.relationship() == FRIEND) {
            //welcome back
            return new EntryAttemptResponseUco(true);
        }
        //not a friend, but we know him
        return new EntryAttemptResponseUco(false);
    }

    @Override
    public void personEntered(PersonId personId) {
        LOG.info("Person entered: " + personId);
    }
}
