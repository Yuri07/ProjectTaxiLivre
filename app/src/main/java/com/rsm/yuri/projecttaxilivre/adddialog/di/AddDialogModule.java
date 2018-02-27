package com.rsm.yuri.projecttaxilivre.adddialog.di;

import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogInteractor;
import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogInteractorImpl;
import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogPresenter;
import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogPresenterImpl;
import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogRepository;
import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.adddialog.ui.AddDialogView;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 27/02/2018.
 */
@Module
public class AddDialogModule {

    private AddDialogView view;

    public AddDialogModule(AddDialogView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    AddDialogView providesAddDialogView(){
        return view;
    }

    @Provides
    @Singleton
    AddDialogPresenter providesAddDialogPresenter(EventBus eventBus, AddDialogView AddDialogView, AddDialogInteractor AddDialogInteractor){
        return new AddDialogPresenterImpl(eventBus, AddDialogView, AddDialogInteractor);
    }

    @Provides
    @Singleton
    AddDialogInteractor providesAddDialogInteractor(AddDialogRepository addDialogRepository){
        return new AddDialogInteractorImpl(addDialogRepository);
    }

    @Provides
    @Singleton
    AddDialogRepository providesAddDialogRepository(FirebaseAPI firebase, EventBus eventBus){
        return new AddDialogRepositoryImpl(firebase, eventBus);
    }

    /*@Provides
    @Singleton
    AddDialogRepositoryImpl providesAddDialogRepositoryImpl(FirebaseAPI firebase, EventBus eventBus){
        return new AddDialogRepositoryImpl(firebase, eventBus);
    }*/

}
