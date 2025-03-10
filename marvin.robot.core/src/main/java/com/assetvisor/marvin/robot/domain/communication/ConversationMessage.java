package com.assetvisor.marvin.robot.domain.communication;

public abstract class ConversationMessage extends Message {

    public static final String DEFAULT_CONVERSATION_ID = "1";
    private final String conversationId;

    public ConversationMessage(String sender, String conversationId) {
        super(sender);
        this.conversationId = conversationId == null ? DEFAULT_CONVERSATION_ID : conversationId;
    }

    public String conversationId() {
        return conversationId;
    }

    @Override
    public String toString() {
        return "ConversationMessage{" +
            "conversationId='" + conversationId + '\'' +
            "} " + super.toString();
    }
}
