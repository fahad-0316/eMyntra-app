package com.example.emyntra;

public class ChatMessage {
    private String message;
    private boolean isUser; // true if User sent it, false if AI sent it

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}