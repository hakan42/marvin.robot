package com.assetvisor.marvin.robot.domain.communication;

public abstract class Message {

    private final String sender;
    private final String conversationId;

    public Message(String sender, String conversationId) {
        this.sender = sender;
        this.conversationId = conversationId;
    }

    public String getSender() {
        return sender;
    }

    public String getConversationId() {
        return conversationId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message message)) {
            return false;
        }

        return sender.equals(message.sender) && conversationId.equals(message.conversationId);
    }

    @Override
    public int hashCode() {
        int result = sender.hashCode();
        result = 31 * result + conversationId.hashCode();
        return result;
    }
}
