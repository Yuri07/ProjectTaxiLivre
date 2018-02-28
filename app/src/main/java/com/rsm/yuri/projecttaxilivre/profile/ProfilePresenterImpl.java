package com.rsm.yuri.projecttaxilivre.profile;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.profile.events.ProfileEvent;
import com.rsm.yuri.projecttaxilivre.profile.ui.ProfileView;

import org.greenrobot.eventbus.Subscribe;



/**
 * Created by yuri_ on 02/01/2018.
 */

public class ProfilePresenterImpl implements ProfilePresenter {

    ProfileView view;
    EventBus eventBus;
    ProfileInteractor profileInteractor;

    public ProfilePresenterImpl( EventBus eventBus, ProfileView view, ProfileInteractor profileInteractor) {
        this.view = view;
        this.eventBus = eventBus;
        this.profileInteractor = profileInteractor;
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
    public void uploadPhoto(Uri selectedImageUri) {
        profileInteractor.execute(selectedImageUri);
    }


    @Override
    @Subscribe
    public void onEventMainThread(ProfileEvent event) {
        String error = event.getError();
        if (this.view != null) {
            switch (event.getType()) {
                case ProfileEvent.UPLOAD_INIT:
                    Log.d("d", "view.onUploadInit");
                    view.onUploadInit();
                    break;
                case ProfileEvent.UPLOAD_COMPLETE:
                    Log.d("d", "view.onUploadComplte(event.getUrlPhotoUser), " + event.getUlrPhotoUser());
                    view.onUploadComplete(event.getUlrPhotoUser());
                    break;
                case ProfileEvent.UPLOAD_ERROR:
                    view.onUploadError(error);
                    break;
            }
        }
    }
}
