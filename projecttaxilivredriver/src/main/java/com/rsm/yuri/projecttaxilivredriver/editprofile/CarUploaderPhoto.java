package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;
import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseStorageFinishedListener;
import com.rsm.yuri.projecttaxilivredriver.editprofile.events.EditProfileEvent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

/**
 * Created by yuri_ on 02/04/2018.
 */

public class CarUploaderPhoto implements UploaderPhoto {

    private EventBus eventBus;
    private FirebaseAPI firebase;

    public CarUploaderPhoto(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void uploadPhoto(Uri selectedImageUri) {
        if(selectedImageUri!=null) {
            Log.d("d", "CarUploaderPhoto.post(url, ProfileEventUPLOAD_INIT)");
            post(EditProfileEvent.UPLOAD_INIT);

            firebase.updateCarPhoto(selectedImageUri, new FirebaseStorageFinishedListener() {

                @Override
                public void onSuccess(String url) {
                    Log.d("d", "CarUploaderPhoto.post(url, ProfileEventUPLOAD_COMPLETE)");
                    post(url, EditProfileEvent.UPLOAD_PHOTO_CAR_COMPLETE);
                }

                @Override
                public void onError(String error) {
                    EditProfileEvent event = new EditProfileEvent();
                    event.setType(EditProfileEvent.UPLOAD_ERROR);
                    event.setError(error);
                    post(EditProfileEvent.UPLOAD_ERROR, error);
                }
            });
        }
    }

    private void post(int type){
        post(type, null, null, null, null);
    }

    private  void post(String url, int type){
        post(type, null, null, url, null);
    }

    private void post(int type, Driver driver, Car car) {
        Log.d("d", "CarUploaderPhoto.post(type, driver, car): "+ driver.getEmail());
        post(type, driver, car, null, null);
    }

    private void post(int type, String errorMsg){
        post(type,null, null, null,errorMsg);
    }

    private void post(int type, Driver driver, Car car, String url,String errorMsg){
        Log.d("d", "CarUploaderPhoto.post(): ");
        EditProfileEvent event = new EditProfileEvent();
        event.setUlrPhoto(url);
        event.setType(type);
        event.setDriver(driver);
        event.setCar(car);
        event.setError(errorMsg);
        eventBus.post(event);
    }

}
