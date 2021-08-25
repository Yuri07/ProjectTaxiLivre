package com.rsm.yuri.projecttaxilivre.profile;

import android.location.Location;
import android.net.Uri;

import com.rsm.yuri.projecttaxilivre.profile.events.ProfileEvent;


/**
 * Created by yuri_ on 02/01/2018.
 */

public interface ProfilePresenter {

    void onCreate();
    void onDestroy();

    void uploadPhoto(Uri selectedImageUri);
    void onEventMainThread(ProfileEvent event);

    void retrieveDataUser();
}
