package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

/**
 * Created by yuri_ on 14/03/2018.
 */

public class EditProfileInteractorImpl implements EditProfileInteractor {

    EditProfileRepository repository;

    public EditProfileInteractorImpl(EditProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveProfile(Driver driver, Car car) {
        repository.saveProfile(driver, car);
    }

    @Override
    public void updateDriver(Driver driver) {
        repository.updateDriver(driver);
    }

    @Override
    public void updateCar(Car car) {
        repository.updateCar(car);
    }

    @Override
    public void uploadPhoto(Uri selectedImageUri) {
        repository.uploadPhoto(selectedImageUri);
    }

    @Override
    public void retrieveDataUser() {
        repository.retrieveDataUser();
    }


    /*@Override
    public void uploadPhotoDriver(Uri selectedImageUri) {
        repository.uploadPhotoDriver(selectedImageUri);
    }

    @Override
    public void uploadPhotoCar(Uri selectedImageUri) {
        repository.uploadPhotoCar(selectedImageUri);
    }*/
}
