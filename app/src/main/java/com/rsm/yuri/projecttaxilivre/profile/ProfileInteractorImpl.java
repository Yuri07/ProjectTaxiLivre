package com.rsm.yuri.projecttaxilivre.profile;

import android.location.Location;
import android.net.Uri;

/**
 * Created by yuri_ on 02/01/2018.
 */

public class ProfileInteractorImpl implements ProfileInteractor {

    private ProfileRepository repository;

    public ProfileInteractorImpl(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(Uri selectedImageUri) {
        repository.uploadPhoto(selectedImageUri);
    }
}
