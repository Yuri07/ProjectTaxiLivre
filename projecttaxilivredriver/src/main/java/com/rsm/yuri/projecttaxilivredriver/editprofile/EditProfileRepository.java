package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

/**
 * Created by yuri_ on 14/03/2018.
 */

public interface EditProfileRepository {

    void saveProfile(Driver driver, Car car);
    void updateDriver(Driver driver);
    void updateCar(Car car);
    void uploadPhoto(Uri selectedImageUri);

}
