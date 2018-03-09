package com.rsm.yuri.projecttaxilivredriver.profile.di;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfileInteractor;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfileInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfilePresenter;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfilePresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfileRepository;
import com.rsm.yuri.projecttaxilivredriver.profile.ProfileRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.profile.ui.ProfileView;

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
        return new ProfileRepositoryImpl(eventBus, firebase);//, imageStorage);
    }
}
