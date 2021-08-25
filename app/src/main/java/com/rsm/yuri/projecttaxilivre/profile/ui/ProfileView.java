package com.rsm.yuri.projecttaxilivre.profile.ui;

import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;

/**
 * Created by yuri_ on 02/01/2018.
 */

public interface ProfileView {

    void onUploadInit();
    void onUploadComplete(String urlPhotoUser);
    void onUploadError(String error);

    void onSuccessToGetDataUser(User currentUser);

    void onFailedToGetDataUser(String error);
}
