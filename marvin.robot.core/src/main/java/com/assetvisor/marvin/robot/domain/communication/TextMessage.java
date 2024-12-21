package com.assetvisor.marvin.robot.domain.communication;

public class TextMessage extends ConversationMessage {
    private final String content;

    public TextMessage(String sender, String conversationId, String content) {
        super(sender, conversationId);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof TextMessage that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TextMessage{" +
            "content='" + content + '\'' +
            "} " + super.toString();
    }
}
