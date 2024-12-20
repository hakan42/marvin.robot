package com.assetvisor.marvin.robot.domain.relationships;

import java.util.Map;

public record Person(String id, String name, String email, Relationship relationship, Map<String, String> externalIds) {
    public enum Relationship {
        FRIEND, FAMILY, COLLEAGUE, ACQUAINTANCE, STRANGER
    }
}
