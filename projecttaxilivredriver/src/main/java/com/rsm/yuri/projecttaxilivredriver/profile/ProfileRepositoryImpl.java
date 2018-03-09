package com.rsm.yuri.projecttaxilivredriver.profile;

import android.net.Uri;
import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseStorageFinishedListener;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.profile.events.ProfileEvent;

/**
 * Created by yuri_ on 08/03/2018.
 */

public class ProfileRepositoryImpl implements ProfileRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;

    public ProfileRepositoryImpl(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
    }

    @Override
    public void uploadPhoto(Uri selectedImageUri) {
        if(selectedImageUri!=null) {
            Log.d("d", "post(url, ProfileEventUPLOAD_INIT)");
            post(ProfileEvent.UPLOAD_INIT);

            firebase.updateAvatarPhoto(selectedImageUri, new FirebaseStorageFinishedListener() {

                @Override
                public void onSuccess(String url) {
                    Log.d("d", "post(url, ProfileEventUPLOAD_COMPLETE)");
                    post(url, ProfileEvent.UPLOAD_COMPLETE);
                }

                @Override
                public void onError(String error) {
                    post(ProfileEvent.UPLOAD_ERROR, error);
                }
            });
        }
    }

    private void post(int type){
        post(null, type, null);
    }

    private void post(String url, int type){
        post(url, type, null);
    }

    private void post(int type, String error){
        post(null, type, error);
    }

    private void post(String url, int type, String error){
        ProfileEvent event = new ProfileEvent();
        event.setUlrPhotoUser(url);
        event.setType(type);
        event.setError(error);
        eventBus.post(event);
    }

}
