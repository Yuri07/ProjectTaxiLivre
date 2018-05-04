package com.rsm.yuri.projecttaxilivre.chat.entities;

import com.google.firebase.database.Exclude;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class ChatMessage {

    private String id;
    private String msg;
    private String sender;

    @Exclude
    private boolean sentByMe;
    private boolean read;

    public ChatMessage(){}

    public ChatMessage(String sender, String msg, boolean read){
        this.msg = msg;
        this.sender = sender;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean isSentByMe() {
        return sentByMe;
    }

    public void setSentByMe(boolean sentByMe) {
        this.sentByMe = sentByMe;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
