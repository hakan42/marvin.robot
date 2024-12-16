package com.assetvisor.marvin.robot.domain.environment;

public record Observation(String itemId, String value, String oldValue, String description) {

    @Override
    public String toString() {
        return "Item: " + itemId + " changed value from " + oldValue + " to " + value + " - " + description;
    }
}
