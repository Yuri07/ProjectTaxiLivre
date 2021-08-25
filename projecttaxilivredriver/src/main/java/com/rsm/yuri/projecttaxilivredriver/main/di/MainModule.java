package com.rsm.yuri.projecttaxilivredriver.main.di;

//import androidx.core.app.Fragment;
//import androidx.core.app.FragmentManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.main.MainInteractor;
import com.rsm.yuri.projecttaxilivredriver.main.MainInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.main.MainPresenter;
import com.rsm.yuri.projecttaxilivredriver.main.MainPresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.main.MainRepository;
import com.rsm.yuri.projecttaxilivredriver.main.MainRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.main.SessionInteractor;
import com.rsm.yuri.projecttaxilivredriver.main.SessionInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 15/01/2018.
 */
@Module
public class MainModule {

    private MainView view;
    private Fragment[] fragments;
    private FragmentManager fragmentManager;

    public MainModule(MainView view, FragmentManager fragmentManager, Fragment[] fragments){
        this.view = view;
        this.fragments = fragments;
        this.fragmentManager = fragmentManager;
    }

    @Provides
    @Singleton
    MainView providesMainView(){
        return this.view;
    }

    @Provides @Singleton
    Fragment[] providesFragmentArray(){
        return this.fragments;
    }

    @Provides
    @Singleton
    FragmentManager providesFragmentManager(){
        return fragmentManager;
    }

    @Provides
    @Singleton
    MainPresenter providesMainPresenter(EventBus eventBus, MainView mainView, MainInteractor mainInteractor, SessionInteractor sessionInteractor) {
        return new MainPresenterImpl(eventBus, mainView, mainInteractor, sessionInteractor);
    }

    @Provides @Singleton
    MainInteractor providesMainInteractor(MainRepository repository) {
        return new MainInteractorImpl(repository);
    }

    @Provides @Singleton
    SessionInteractor providesSessionInteractor(MainRepository repository) {
        return new SessionInteractorImpl(repository);
    }

    @Provides @Singleton
    MainRepository providesMainRepository(FirebaseAPI firebase, EventBus eventBus) {
        return new MainRepositoryImpl(eventBus, firebase);
    }

}
