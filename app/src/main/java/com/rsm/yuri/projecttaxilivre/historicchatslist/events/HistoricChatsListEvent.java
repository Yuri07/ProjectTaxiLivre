package com.rsm.yuri.projecttaxilivre.historicchatslist.events;

import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListEvent {

    private Driver driver;
    private int eventType;
    private String error;

    public final static int onHistoricChatAdded = 0;
    public final static int onHistoricChatChanged = 1;
    public final static int onHistoricChatRemoved = 2;
    public final static int ERROR_EVENT = 3;


    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
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
