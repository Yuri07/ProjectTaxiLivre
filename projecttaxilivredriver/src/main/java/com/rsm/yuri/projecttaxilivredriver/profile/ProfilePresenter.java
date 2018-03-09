package com.rsm.yuri.projecttaxilivredriver.profile;

import android.net.Uri;

import com.rsm.yuri.projecttaxilivredriver.profile.events.ProfileEvent;

/**
 * Created by yuri_ on 08/03/2018.
 */

public interface ProfilePresenter {
    void onCreate();
    void onDestroy();

    void uploadPhoto(Uri selectedImageUri);
    void onEventMainThread(ProfileEvent event);
}
