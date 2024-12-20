package com.assetvisor.marvin.persistence.adapters.cassandra.relationships;

import com.assetvisor.marvin.robot.domain.relationships.ForAddingPerson;
import com.assetvisor.marvin.robot.domain.relationships.ForGettingPerson;
import com.assetvisor.marvin.robot.domain.relationships.Person;
import com.assetvisor.marvin.robot.domain.relationships.Person.Relationship;
import jakarta.annotation.Resource;
import java.util.Map;
import java.util.Optional;
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
        PersonEntry personEntry = new PersonEntry();
        personEntry.setId(UUID.randomUUID());
        personEntry.setPersonName(person.name());
        personEntry.setEmail(person.email());
        personEntry.setRelationship(person.relationship().name());
        personEntry.setGithubId(person.externalIds().get("GITHUB"));
        return personEntry;
    }

    private Person toPerson(PersonEntry personEntry) {
        return new Person(
            personEntry.getId().toString(),
            personEntry.getPersonName(),
            personEntry.getEmail(),
            Relationship.valueOf(personEntry.getRelationship()),
            Optional.ofNullable(personEntry.getGithubId())
                .map(githubId ->
                    Map.of("GITHUB", githubId)
                )
                .orElse(Map.of())
        );
    }
}
