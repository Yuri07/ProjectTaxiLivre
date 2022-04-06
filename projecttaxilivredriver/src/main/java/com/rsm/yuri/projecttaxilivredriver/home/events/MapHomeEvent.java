package com.rsm.yuri.projecttaxilivredriver.home.events;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class MapHomeEvent {

    private String newTravelID;
    private int eventType;
    private String error;

    public static int ON_TRAVEL_CREATED = 22;

    public final static int onSuccessToGetDateUser = 101;
    public final static int onFailedToGetDateUser = 102;

    public String getNewTravelID() {
        return newTravelID;
    }

    public void setNewTravelID(String newTravelID) {
        this.newTravelID = newTravelID;
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
