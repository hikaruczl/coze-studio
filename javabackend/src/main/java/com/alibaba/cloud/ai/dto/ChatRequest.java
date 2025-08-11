package com.alibaba.cloud.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatRequest {

    @JsonProperty("bot_id")
    private Long botId;

    @JsonProperty("conversation_id")
    private Long conversationId;

    private String query;

    private String scene;

    // Getters and Setters
    public Long getBotId() {
        return botId;
    }

    public void setBotId(Long botId) {
        this.botId = botId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}
