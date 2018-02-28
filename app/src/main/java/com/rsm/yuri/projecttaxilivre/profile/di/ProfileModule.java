package com.rsm.yuri.projecttaxilivre.profile.di;

import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageStorage;
import com.rsm.yuri.projecttaxilivre.profile.ProfileInteractor;
import com.rsm.yuri.projecttaxilivre.profile.ProfileInteractorImpl;
import com.rsm.yuri.projecttaxilivre.profile.ProfilePresenter;
import com.rsm.yuri.projecttaxilivre.profile.ProfilePresenterImpl;
import com.rsm.yuri.projecttaxilivre.profile.ProfileRepository;
import com.rsm.yuri.projecttaxilivre.profile.ProfileRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.profile.ui.ProfileView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 27/02/2018.
 */
@Module
public class ProfileModule {
    ProfileView view;

    public ProfileModule(ProfileView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    ProfileView providesProfileView(){
        return view;
    }

    @Provides
    @Singleton
    ProfilePresenter providesProfilePresenter(EventBus eventBus, ProfileView profileView, ProfileInteractor profileInteractor){
        return new ProfilePresenterImpl(eventBus, profileView, profileInteractor);
    }

    @Provides
    @Singleton
    ProfileInteractor providesProfileInteractor(ProfileRepository profileRepository){
        return new ProfileInteractorImpl(profileRepository);
    }

    @Provides
    @Singleton
    ProfileRepository providesProfileRepository(FirebaseAPI firebase, EventBus eventBus){//, ImageStorage imageStorage){
        return new ProfileRepositoryImpl(firebase, eventBus);//, imageStorage);
    }
}
