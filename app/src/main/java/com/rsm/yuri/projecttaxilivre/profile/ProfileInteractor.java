package com.rsm.yuri.projecttaxilivre.profile;

import android.location.Location;
import android.net.Uri;

/**
 * Created by yuri_ on 02/01/2018.
 */

public interface ProfileInteractor {

    void execute(Uri selectedImageUri);

    void retrieveDataUser();

}
