package com.rsm.yuri.projecttaxilivre.map.events;

import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 30/01/2018.
 */

public class MapEvent {

    private Driver driver;
    private int eventType;
    private String error;

    public final static int onDriverAdded = 0;
    public final static int onDriverMoved = 1;
    public final static int onDriverRemoved = 2;

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
