package com.rsm.yuri.projecttaxilivre.map.entities;

/**
 * Created by yuri_ on 30/01/2018.
 */

public class Location {

    private double latitutde;
    private double longitude;

    public Location() {
    }

    public Location(double latitutde, double longitude) {
        this.latitutde = latitutde;
        this.longitude = longitude;
    }

    public double getLatitutde() {
        return latitutde;
    }

    public void setLatitutde(double latitutde) {
        this.latitutde = latitutde;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
