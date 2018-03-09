package com.rsm.yuri.projecttaxilivredriver.profile.ui;

/**
 * Created by yuri_ on 08/03/2018.
 */

public interface ProfileView {
    void onUploadInit();
    void onUploadComplete(String urlPhotoUser);
    void onUploadError(String error);
}
