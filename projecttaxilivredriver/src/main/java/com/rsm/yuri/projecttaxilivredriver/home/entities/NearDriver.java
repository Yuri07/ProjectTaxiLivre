package com.rsm.yuri.projecttaxilivredriver.home.entities;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class NearDriver {

    private String email;
    private double latitude;
    private double longitude;

    public NearDriver() {
    }

    public NearDriver(String email, double latitude, double longitude) {
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
