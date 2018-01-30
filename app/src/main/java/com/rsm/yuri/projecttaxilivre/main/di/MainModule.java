package com.rsm.yuri.projecttaxilivre.main.di;

import android.support.v4.app.FragmentManager;

import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.main.MainInteractor;
import com.rsm.yuri.projecttaxilivre.main.ui.MainView;
import com.rsm.yuri.projecttaxilivre.main.MainPresenter;
import com.rsm.yuri.projecttaxilivre.main.MainInteractorImpl;
import com.rsm.yuri.projecttaxilivre.main.MainPresenterImpl;
import com.rsm.yuri.projecttaxilivre.main.MainRepository;
import com.rsm.yuri.projecttaxilivre.main.MainRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.main.SessionInteractor;
import com.rsm.yuri.projecttaxilivre.main.SessionInteractorImpl;
import com.rsm.yuri.projecttaxilivre.map.ui.MapFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 15/01/2018.
 */
@Module
public class MainModule {

    private MainView view;
    private MapFragment fragment;
    private FragmentManager fragmentManager;

    public MainModule(MainView view, FragmentManager fragmentManager, MapFragment mapFragment){
        this.view = view;
        this.fragment = mapFragment;
        this.fragmentManager = fragmentManager;
    }

    @Provides
    @Singleton
    MainView providesMainView(){
        return this.view;
    }

    @Provides
    @Singleton
    MapFragment providesMainFragment(){
        return fragment;
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
