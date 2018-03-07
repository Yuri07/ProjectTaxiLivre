package com.rsm.yuri.projecttaxilivredriver.historicchatslist.events;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListEvent {

    private User user;
    private int eventType;
    private String error;

    public final static int onHistoricChatAdded = 0;
    public final static int onHistoricChatChanged = 1;
    public final static int onHistoricChatRemoved = 2;
    public final static int ERROR_EVENT = 3;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
