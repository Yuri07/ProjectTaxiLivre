package com.rsm.yuri.projecttaxilivredriver.editprofile.events;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

/**
 * Created by yuri_ on 14/03/2018.
 */

public class EditProfileEvent {

    private Driver driver;
    private Car car;
    private String ulrPhotoUser;
    private int type;
    private String error;
    public final static int UPLOAD_INIT = 0;
    public final static int UPLOAD_COMPLETE = 1;
    public final static int UPLOAD_ERROR = 2;
    public final static int SAVE_PROFILE_SUCESS = 3;
    public final static int SAVE_PROFILE_ERROR = 4;

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getUlrPhotoUser() {
        return ulrPhotoUser;
    }

    public void setUlrPhotoUser(String ulrPhotoUser) {
        this.ulrPhotoUser = ulrPhotoUser;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
