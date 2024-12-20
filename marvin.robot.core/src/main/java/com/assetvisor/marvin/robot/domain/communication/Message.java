package com.assetvisor.marvin.robot.domain.communication;

public abstract class Message {

    private final String sender;

    public Message(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }
}
