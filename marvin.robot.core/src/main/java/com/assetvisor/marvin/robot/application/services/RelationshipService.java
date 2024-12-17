package com.assetvisor.marvin.robot.application.services;

import com.assetvisor.marvin.robot.application.AddStrangerUseCase;
import com.assetvisor.marvin.robot.application.FindPersonUseCase;
import com.assetvisor.marvin.robot.domain.relationships.ForAddingPerson;
import com.assetvisor.marvin.robot.domain.relationships.ForGettingPerson;
import com.assetvisor.marvin.robot.domain.relationships.Person;
import jakarta.annotation.Resource;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class RelationshipService implements FindPersonUseCase, AddStrangerUseCase {

    @Resource
    private ForGettingPerson forGettingPerson;
    @Resource
    private ForAddingPerson forAddingPerson;

    @Override
    public void addStranger(String name, String email) {
        forAddingPerson.addPerson(new Person(name, email, Person.Relationship.STRANGER));
    }

    @Override
    public PersonUco findPersonByEmail(String email) {
        return Optional.ofNullable(forGettingPerson.getPerson(email))
            .map(p -> new PersonUco(p.email(), p.relationship()))
            .orElse(null);
    }
}
