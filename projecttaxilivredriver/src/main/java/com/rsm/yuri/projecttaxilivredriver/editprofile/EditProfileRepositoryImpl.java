package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.editprofile.events.EditProfileEvent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

/**
 * Created by yuri_ on 14/03/2018.
 */

public class EditProfileRepositoryImpl implements EditProfileRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;

    public EditProfileRepositoryImpl(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void saveProfile(final Driver driver, final Car car) {
        //updateKeyValueDriver(TaxiLivreDriverApp.NOME_KEY, driver.getNome());
        //updateCar(car);
        firebase.updateProfile(driver, car, new FirebaseActionListenerCallback(){

            @Override
            public void onSuccess() {
                post(EditProfileEvent.SAVE_PROFILE_SUCESS, driver, car);
            }

            @Override
            public void onError(DatabaseError error) {
                post(EditProfileEvent.SAVE_PROFILE_ERROR, error.getMessage());
            }
        });
    }

    @Override
    public void uploadPhoto(Uri selectedImageUri) {

    }

    @Override
    public void updateDriver(Driver driver) {

    }

    @Override
    public void updateCar(Car car) {

    }

    private void post(int type, Driver driver, Car car) {
        Log.d("d", "EditProfileRepository.post(type, driver, car): "+ driver.getEmail());
        post(type, driver, car, null);
    }

    private void post(int type, String errorMsg){
        post(type,null, null, errorMsg);
    }

    private void post(int type, Driver driver, Car car, String errorMsg){
        Log.d("d", "EditProfileRepository.post(): ");
        EditProfileEvent event = new EditProfileEvent();
        event.setType(type);
        event.setDriver(driver);
        event.setCar(car);
        event.setError(errorMsg);
        eventBus.post(event);
    }

}
