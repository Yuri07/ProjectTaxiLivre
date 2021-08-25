package com.rsm.yuri.projecttaxilivre.map.events;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;

/**
 * Created by yuri_ on 30/01/2018.
 */

public class MapEvent {

    private NearDriver nearDriver;
    private String travelAck;
    private LatLng locationOfMyDriver;
    private int eventType;
    private String error;
    private User currentUser;

    public final static int onDriverAdded = 0;
    public final static int onDriverMoved = 2;
    public final static int onDriverRemoved = 3;
    public static int onTravelAckReceived = 4;
    public final static int onMyDriverMoved = 5;

    public final static int onSuccessToGetDateUser = 101;
    public final static int onFailedToGetDateUser = 102;

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

    public LatLng getLocationOfMyDriver() {
        return locationOfMyDriver;
    }

    public void setLocationOfMyDriver(LatLng locationOfMyDriver) {
        this.locationOfMyDriver = locationOfMyDriver;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
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
