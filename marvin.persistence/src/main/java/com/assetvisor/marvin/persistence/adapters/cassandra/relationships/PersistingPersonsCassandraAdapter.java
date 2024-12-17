package com.assetvisor.marvin.persistence.adapters.cassandra.relationships;

import com.assetvisor.marvin.robot.domain.relationships.ForAddingPerson;
import com.assetvisor.marvin.robot.domain.relationships.ForGettingPerson;
import com.assetvisor.marvin.robot.domain.relationships.Person;
import com.assetvisor.marvin.robot.domain.relationships.Person.Relationship;
import jakarta.annotation.Resource;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
//@Profile("cassandra")
public class PersistingPersonsCassandraAdapter implements ForAddingPerson, ForGettingPerson {

    @Resource
    private PersonRepository personRepository;

    @Override
    public void addPerson(Person person) {
        personRepository.findByEmail(person.email()).stream()
            .findAny()
            .ifPresent(_ -> {
                    throw new PersonExistsException("Person with email " + person.email() + " already exists");
                }
            );
        personRepository.save(toPersonEntry(person));
    }

    @Override
    public Person getPerson(String email) {
        return personRepository.findByEmail(email).stream()
            .findAny()
            .map(this::toPerson)
            .orElse(null);
    }

    private PersonEntry toPersonEntry(Person person) {
        return new PersonEntry(
            UUID.randomUUID(),
            person.name(),
            person.email(),
            person.relationship().name()
        );
    }

    private Person toPerson(PersonEntry personEntry) {
        return new Person(
            personEntry.getPersonName(),
            personEntry.getEmail(),
            Relationship.valueOf(personEntry.getRelationship())
        );
    }
}
