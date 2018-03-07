package com.rsm.yuri.projecttaxilivredriver.adddialog.di;

import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogInteractor;
import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogPresenter;
import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogPresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogRepository;
import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.adddialog.ui.AddDialogView;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

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
