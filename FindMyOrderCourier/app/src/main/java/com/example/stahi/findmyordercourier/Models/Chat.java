package com.example.stahi.findmyordercourier.Models;

public class Chat {

    public String sender;
    public String receiver;
    public String senderName;
    public String receiverName;
    public String senderUid;
    public String receiverUid;
    public String message;
    public long timestamp;
    public String senderFirebaseToken;
    public String receiverFirebaseToken;

    public boolean IsChatEmpty;

    public Chat() {

    }

    public Chat(String sender, String receiver, String senderUid, String receiverUid, String message, long timestamp, String senderFirebaseToken, String receiverFirebaseToken, String senderName, String receiverName) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
        this.senderFirebaseToken = senderFirebaseToken;
        this.receiverFirebaseToken = receiverFirebaseToken;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }
}