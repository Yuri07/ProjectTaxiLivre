package com.rsm.yuri.projecttaxilivredriver.editprofile.di;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileInteractor;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfilePresenter;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfilePresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileRepository;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.editprofile.ui.EditProfileView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 14/03/2018.
 */

@Module
public class EditProfileModule {

    private EditProfileView view;

    public EditProfileModule(EditProfileView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    EditProfileView providesEditProfileView(){
        return view;
    }

    @Provides
    @Singleton
    EditProfilePresenter providesEditProfilePresenter(EventBus eventBus, EditProfileView editProfileView, EditProfileInteractor editProfileInteractor){
        return new EditProfilePresenterImpl(editProfileView, eventBus, editProfileInteractor);
    }

    @Provides
    @Singleton
    EditProfileInteractor providesAddDialogInteractor(EditProfileRepository editProfileRepository){
        return new EditProfileInteractorImpl(editProfileRepository);
    }

    @Provides
    @Singleton
    EditProfileRepository providesAddDialogRepository(FirebaseAPI firebase, EventBus eventBus){
        return new EditProfileRepositoryImpl(eventBus, firebase);
    }

}
