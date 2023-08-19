package digital.ivan.lightchain.core.context.model;

import static digital.ivan.lightchain.core.context.model.ChatState.ACTIVE;

public class SessionState {

    private ChatState chatState = ACTIVE;
    private long lastUpdate;

    public SessionState(ChatState chatState) {
        this.chatState = chatState;
    }

    public ChatState getChatState() {
        return chatState;
    }

    public void setChatState(ChatState chatState) {
        this.chatState = chatState;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

