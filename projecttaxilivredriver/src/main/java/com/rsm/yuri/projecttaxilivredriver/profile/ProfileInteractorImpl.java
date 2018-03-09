package com.rsm.yuri.projecttaxilivredriver.profile;

import android.net.Uri;

/**
 * Created by yuri_ on 08/03/2018.
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
