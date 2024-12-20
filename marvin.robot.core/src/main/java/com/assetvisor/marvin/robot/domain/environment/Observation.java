package com.assetvisor.marvin.robot.domain.environment;

import com.assetvisor.marvin.robot.domain.communication.Message;
import java.util.Objects;

public final class Observation extends Message {

    private final String itemId;
    private final String value;
    private final String oldValue;
    private final String description;

    public Observation(String sender, String itemId, String value, String oldValue, String description) {
        super(sender);
        this.itemId = itemId;
        this.value = value;
        this.oldValue = oldValue;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Item: " + itemId + " changed value from " + oldValue + " to " + value + " - " + description;
    }

    public String itemId() {
        return itemId;
    }

    public String value() {
        return value;
    }

    public String oldValue() {
        return oldValue;
    }

    public String description() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Observation) obj;
        return Objects.equals(this.itemId, that.itemId) &&
            Objects.equals(this.value, that.value) &&
            Objects.equals(this.oldValue, that.oldValue) &&
            Objects.equals(this.description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, value, oldValue, description);
    }

}
