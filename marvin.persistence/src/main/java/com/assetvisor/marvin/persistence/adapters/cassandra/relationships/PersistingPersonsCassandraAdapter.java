package com.assetvisor.marvin.persistence.adapters.cassandra.relationships;

import com.assetvisor.marvin.robot.domain.relationships.ForPersistingPerson;
import com.assetvisor.marvin.robot.domain.relationships.Person;
import com.assetvisor.marvin.robot.domain.relationships.Person.IdType;
import com.assetvisor.marvin.robot.domain.relationships.Person.Relationship;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
//@Profile("cassandra")
public class PersistingPersonsCassandraAdapter implements ForPersistingPerson {

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
    public void addExternalId(Person person, Person.PersonId externalId) {
        PersonEntry personEntry = personRepository.findById(UUID.fromString(person.id()))
            .orElseThrow(() -> new IllegalArgumentException("Person with id " + person.id() + " not found"));
        if(personEntry.getGithubId().isPresent() && externalId.idType() == Person.IdType.GITHUB) {
            throw new IllegalArgumentException("Person with id " + person.id() + " already has github id");
        }
        if(personEntry.getGoogleId().isPresent() && externalId.idType() == Person.IdType.GOOGLE) {
            throw new IllegalArgumentException("Person with id " + person.id() + " already has google id");
        }
        if(externalId.idType() == Person.IdType.GITHUB) {
            personEntry.setGithubId(externalId.value());
        }
        if(externalId.idType() == Person.IdType.GOOGLE) {
            personEntry.setGoogleId(externalId.value());
        }
        personRepository.save(personEntry);
    }

    @Override
    public Person byId(String id) {
        return personRepository.findById(UUID.fromString(id))
            .map(this::toPerson)
            .orElse(null);
    }

    @Override
    public Person byEmail(String email) {
        return personRepository.findByEmail(email).stream()
            .findAny()
            .map(this::toPerson)
            .orElse(null);
    }

    @Override
    public Person byExternalId(Person.PersonId externalId) {
        var entries = switch(externalId.idType()) {
            case GOOGLE -> personRepository.findByGoogleId(externalId.value());
            case GITHUB -> personRepository.findByGithubId(externalId.value());
            case LOCAL -> personRepository.findByEmail(externalId.value());
            default -> throw new IllegalArgumentException("Unsupported external id type: " + externalId.idType());
        };
        return entries.stream()
                .findAny()
                .map(this::toPerson)
                .orElse(null);
    }

    private PersonEntry toPersonEntry(Person person) {
        PersonEntry personEntry = new PersonEntry();
        personEntry.setId(UUID.randomUUID());
        personEntry.setPersonName(person.name());
        personEntry.setEmail(person.email());
        personEntry.setPassword(person.password());
        personEntry.setRelationship(person.relationship().name());
        personEntry.setGithubId(person.externalIds().stream()
            .filter(externalId -> externalId.idType() == IdType.GITHUB)
            .map(Person.PersonId::value)
            .findAny()
            .orElse(null)
        );
        personEntry.setGoogleId(person.externalIds().stream()
            .filter(externalId -> externalId.idType() == IdType.GOOGLE)
            .map(Person.PersonId::value)
            .findAny()
            .orElse(null)
        );
        return personEntry;
    }

    private Person toPerson(PersonEntry personEntry) {
        List<Person.PersonId> externalIds = new ArrayList<>();
        personEntry.getGithubId().ifPresent(
            githubId -> externalIds.add(new Person.PersonId(IdType.GITHUB, githubId))
        );
        personEntry.getGoogleId().ifPresent(
            googleId -> externalIds.add(new Person.PersonId(IdType.GOOGLE, googleId))
        );
        return new Person(
            personEntry.getId().toString(),
            personEntry.getPersonName(),
            personEntry.getEmail(),
            personEntry.getPassword().orElse(null),
            Relationship.valueOf(personEntry.getRelationship()),
            externalIds
        );
    }
}
