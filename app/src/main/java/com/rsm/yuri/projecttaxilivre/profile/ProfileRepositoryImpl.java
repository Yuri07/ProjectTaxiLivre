package com.rsm.yuri.projecttaxilivre.profile;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseStorageFinishedListener;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageStorage;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageStorageFinishedListener;
import com.rsm.yuri.projecttaxilivre.profile.events.ProfileEvent;

import java.io.File;



/**
 * Created by yuri_ on 02/01/2018.
 */

public class ProfileRepositoryImpl implements ProfileRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;
    //private ImageStorage imageStorage;

    public ProfileRepositoryImpl( FirebaseAPI firebase, EventBus eventBus){//, ImageStorage imageStorage) {
        this.eventBus = eventBus;
        this.firebase = firebase;
        //this.imageStorage = imageStorage;
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

        /*final String newPhotoId = firebase.create();
        final Photo photo = new Photo();
        photo.setId(newPhotoId);
        photo.setEmail(firebase.getAuthEmail());
        if (location != null) {
            photo.setLatitutde(location.getLatitude());
            photo.setLongitude(location.getLongitude());
        }

        post(MainEvent.UPLOAD_INIT);
        imageStorage.upload(new File(path), photo.getId(), new ImageStorageFinishedListener(){

            @Override
            public void onSuccess() {
                String url = imageStorage.getImageUrl(photo.getId());
                photo.setUrl(url);
                firebase.update(photo);

                post(MainEvent.UPLOAD_COMPLETE);
            }

            @Override
            public void onError(String error) {
                post(MainEvent.UPLOAD_ERROR, error);
            }
        });*/
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
