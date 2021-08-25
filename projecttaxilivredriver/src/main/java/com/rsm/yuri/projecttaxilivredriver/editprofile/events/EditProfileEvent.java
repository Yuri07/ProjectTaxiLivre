package com.rsm.yuri.projecttaxilivredriver.editprofile.events;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

/**
 * Created by yuri_ on 14/03/2018.
 */

public class EditProfileEvent {

    private Driver driver;
    private Car car;
    private String ulrPhoto;
    private int type;
    private String error;
    public final static int UPLOAD_INIT = 0;
    public final static int UPLOAD_PHOTO_DRIVER_COMPLETE = 1;
    public final static int UPLOAD_PHOTO_CAR_COMPLETE = 2;
    public final static int UPLOAD_ERROR = 3;
    public final static int SAVE_PROFILE_SUCESS = 4;
    public final static int SAVE_PROFILE_ERROR = 5;

    public final static int onSuccessToGetDateUser = 6;
    public final static int onFailedToGetDateUser = 7;

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

    public String getUlrPhoto() {
        return ulrPhoto;
    }

    public void setUlrPhoto(String ulrPhoto) {
        this.ulrPhoto = ulrPhoto;
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
