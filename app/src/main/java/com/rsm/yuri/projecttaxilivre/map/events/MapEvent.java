package com.rsm.yuri.projecttaxilivre.map.events;

import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;

/**
 * Created by yuri_ on 30/01/2018.
 */

public class MapEvent {

    private NearDriver nearDriver;
    private String travelAck;
    private int eventType;
    private String error;

    public final static int onDriverAdded = 0;
    public final static int onDriverMoved = 2;
    public final static int onDriverRemoved = 3;
    public static int onTravelAckReceived = 4;

    public NearDriver getNearDriver() {
        return nearDriver;
    }

    public void setNearDriver(NearDriver nearDriver) {
        this.nearDriver = nearDriver;
    }

    public String getTravelAck() {
        return travelAck;
    }

    public void setTravelAck(String travelAck) {
        this.travelAck = travelAck;
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
