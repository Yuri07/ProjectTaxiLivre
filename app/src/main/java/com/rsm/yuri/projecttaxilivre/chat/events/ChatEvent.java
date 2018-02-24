package com.rsm.yuri.projecttaxilivre.chat.events;

import com.rsm.yuri.projecttaxilivre.chat.entities.ChatMessage;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class ChatEvent {

    private ChatMessage msg;
    private int eventType;
    private String error;

    public final static int READ_EVENT = 1;
    public final static int ERROR_EVENT = 0;

    public ChatMessage getMsg() {
        return msg;
    }

    public void setMsg(ChatMessage msg) {
        this.msg = msg;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
