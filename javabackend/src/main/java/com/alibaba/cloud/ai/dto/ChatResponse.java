package com.alibaba.cloud.ai.dto;

public class ChatResponse {

    private String message;

    public ChatResponse(String message) {
        this.message = message;
    }

    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
