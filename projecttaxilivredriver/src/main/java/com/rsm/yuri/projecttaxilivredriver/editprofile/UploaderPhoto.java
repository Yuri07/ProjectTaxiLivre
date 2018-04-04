package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;

import com.rsm.yuri.projecttaxilivredriver.editprofile.events.EditProfileEvent;

/**
 * Created by yuri_ on 02/04/2018.
 */

public interface UploaderPhoto {
    void uploadPhoto(Uri selectedImageUri);
}
