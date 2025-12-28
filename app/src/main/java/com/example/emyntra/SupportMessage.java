package com.example.emyntra;

import java.util.Date;

public class SupportMessage {
    private String messageText;
    private String senderId;
    private long timestamp;

    // Empty constructor needed for Firestore serialization
    public SupportMessage() {
    }

    public SupportMessage(String messageText, String senderId) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.timestamp = new Date().getTime(); // Current time
    }

    // Getters
    public String getMessageText() {
        return messageText;
    }

    public String getSenderId() {
        return senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}