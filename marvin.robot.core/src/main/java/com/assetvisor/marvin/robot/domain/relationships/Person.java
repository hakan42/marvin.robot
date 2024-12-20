package com.assetvisor.marvin.robot.domain.relationships;

import java.util.List;

public record Person(String id, String name, String email, Relationship relationship, List<ExternalId> externalIds) {
    public enum Relationship {
        FRIEND, FAMILY, COLLEAGUE, ACQUAINTANCE, STRANGER
    }
    public enum ExternalIdType {
        GITHUB, MICROSOFT, GOOGLE
    }
    public record ExternalId(ExternalIdType externalIdType, String externalId) {}
}
