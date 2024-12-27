package com.assetvisor.marvin.robot.domain.communication;

import java.time.LocalDateTime;

public abstract class Message {

    private final String sender;
    private final LocalDateTime timestamp;


    public Message(String sender) {
        this.sender = sender;
        this.timestamp = LocalDateTime.now();
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message message)) {
            return false;
        }

        return sender.equals(message.sender);
    }

    @Override
    public int hashCode() {
        return sender.hashCode();
    }

    @Override
    public String toString() {
        return "Message{" +
            "sender='" + sender + '\'' +
            '}';
    }
}
