package com.assetvisor.marvin.robot.domain.relationships;

import java.util.List;

public record Person(
    String id,
    String name,
    String email,
    Relationship relationship,
    List<PersonId> externalIds
) {

    public boolean hasExternalId(PersonId id) {
        return externalIds.stream()
            .anyMatch(externalId -> externalId.idType() == id.idType() && externalId.value().equals(id.value()));
    }

    public void addExternalId(PersonId externalId, ForPersistingPerson forPersistingPerson) {
        externalIds.add(externalId);
        forPersistingPerson.addExternalId(this, externalId);
    }

    public record PersonId(IdType idType, String value) {}

    public enum Relationship {
        FRIEND, FAMILY, COLLEAGUE, ACQUAINTANCE, STRANGER
    }

    public enum IdType {
        GITHUB, MICROSOFT, GOOGLE, LOCAL;

        public static IdType fromRegistrationId(String registrationId) {
            return switch (registrationId) {
                case "github" -> IdType.GITHUB;
                case "google" -> IdType.GOOGLE;
                default -> throw new IllegalArgumentException("Unsupported registrationId: " + registrationId);
            };
        }
    }
}
