package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;
import android.util.Log;

/**
 * Created by yuri_ on 03/04/2018.
 */

public class NullUploaderPhoto implements UploaderPhoto {
    @Override
    public void uploadPhoto(Uri selectedImageUri) {
        Log.d("d", "NullUploaderPhoto Initialized.");
    }
}
