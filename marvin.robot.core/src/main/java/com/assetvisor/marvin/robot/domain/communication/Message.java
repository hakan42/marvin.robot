package com.assetvisor.marvin.robot.domain.communication;

public abstract class Message {

    private final String sender;

    public Message(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
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
