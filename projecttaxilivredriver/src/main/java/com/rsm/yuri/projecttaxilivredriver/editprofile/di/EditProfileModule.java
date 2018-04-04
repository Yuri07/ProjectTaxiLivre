package com.rsm.yuri.projecttaxilivredriver.editprofile.di;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.editprofile.AvatarUploaderPhoto;
import com.rsm.yuri.projecttaxilivredriver.editprofile.CarUploaderPhoto;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileInteractor;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfilePresenter;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfilePresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileRepository;
import com.rsm.yuri.projecttaxilivredriver.editprofile.EditProfileRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.editprofile.NullUploaderPhoto;
import com.rsm.yuri.projecttaxilivredriver.editprofile.UploaderPhoto;
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
    private int uploaderPhoto;

    public final static int UPLOADER_NULL = 0;
    public final static int UPLOADER_AVATAR = 1;
    public final static int UPLOADER_CAR = 2;

    public EditProfileModule(EditProfileView view, int uploaderPhoto) {
        this.view = view;
        this.uploaderPhoto = uploaderPhoto;
    }

    public void setUploaderPhoto(int uploaderPhoto){
        this.uploaderPhoto = uploaderPhoto;
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
    EditProfileInteractor providesEditProfileInteractor(EditProfileRepository editProfileRepository){
        return new EditProfileInteractorImpl(editProfileRepository);
    }

    @Provides
    @Singleton
    EditProfileRepository providesEditProfileRepository(FirebaseAPI firebase, EventBus eventBus, UploaderPhoto uploaderPhoto){
        return new EditProfileRepositoryImpl(eventBus, firebase, uploaderPhoto);
    }
    @Provides
    @Singleton
    UploaderPhoto providesUploaderPhoto(FirebaseAPI firebase, EventBus eventBus){

        if(uploaderPhoto==UPLOADER_AVATAR){
            return new AvatarUploaderPhoto(eventBus, firebase);
        }else if (uploaderPhoto==UPLOADER_CAR) {
            return new CarUploaderPhoto(eventBus, firebase);
        }
        return new NullUploaderPhoto();
    }

}
