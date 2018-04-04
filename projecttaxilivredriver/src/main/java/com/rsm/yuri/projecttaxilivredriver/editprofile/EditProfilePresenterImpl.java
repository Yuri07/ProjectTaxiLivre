package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;
import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.editprofile.events.EditProfileEvent;
import com.rsm.yuri.projecttaxilivredriver.editprofile.ui.EditProfileView;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 14/03/2018.
 */

public class EditProfilePresenterImpl implements EditProfilePresenter {

    EditProfileView view;
    EventBus eventBus;
    EditProfileInteractor editprofileInteractor;

    public EditProfilePresenterImpl(EditProfileView view, EventBus eventBus, EditProfileInteractor editProfileInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.editprofileInteractor = editProfileInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        view = null;
        eventBus.unregister(this);
    }

    @Override
    public void saveProfile(Driver driver, Car car) {
        view.hideInput();
        view.showProgress();
        editprofileInteractor.saveProfile(driver, car);
    }

    @Override
    public void updateDriver(Driver driver) {
        editprofileInteractor.updateDriver(driver);
    }

    @Override
    public void updateCar(Car car) {
        editprofileInteractor.updateCar(car);
    }

    @Override
    public void uploadPhoto(Uri selectedImageUri) {
        editprofileInteractor.uploadPhoto(selectedImageUri);
    }

    /*@Override
    public void uploadPhotoDriver(Uri selectedImageUri) {
        editprofileInteractor.uploadPhotoDriver(selectedImageUri);
    }

    @Override
    public void uploadPhotoCar(Uri selectedImageUri) {
        editprofileInteractor.uploadPhotoCar(selectedImageUri);
    }*/

    @Override
    @Subscribe
    public void onEventMainThread(EditProfileEvent event) {
        Log.d("d", "onEventMainThread(): "+event.getType());
        String error = event.getError();
        if (this.view != null) {
            view.hideProgress();
            view.showInput();
            switch (event.getType()) {
                case EditProfileEvent.UPLOAD_INIT:
                    //Log.d("d", "view.onUploadInit");
                    view.onUploadInit();
                    break;
                case EditProfileEvent.UPLOAD_PHOTO_DRIVER_COMPLETE:
                    //Log.d("d", "view.onUploadComplte(event.getUrlPhotoUser), " + event.getUlrPhotoUser());
                    view.onUploadPhotoDriverComplete(event.getUlrPhoto());
                    break;
                case EditProfileEvent.UPLOAD_PHOTO_CAR_COMPLETE:
                    //Log.d("d", "view.onUploadComplte(event.getUrlPhotoUser), " + event.getUlrPhotoUser());
                    view.onUploadPhotoCarComplete(event.getUlrPhoto());
                    break;
                case EditProfileEvent.UPLOAD_ERROR:
                    view.onUploadError(error);
                    break;
                case EditProfileEvent.SAVE_PROFILE_SUCESS:
                    Log.d("d", "view.profileEdited() chamado");
                    view.profileEdited(event.getDriver(), event.getCar());
                    break;
                case EditProfileEvent.SAVE_PROFILE_ERROR:
                    view.profileNotEdited(event.getError());
                    break;
            }
        }
    }
}
