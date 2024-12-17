package com.assetvisor.marvin.robot.domain.relationships;

public record Person(String name, String email, Relationship relationship) {
    public enum Relationship {
        FRIEND, FAMILY, COLLEAGUE, ACQUAINTANCE, STRANGER
    }
}
