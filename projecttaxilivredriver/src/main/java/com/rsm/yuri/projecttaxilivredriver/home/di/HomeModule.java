package com.rsm.yuri.projecttaxilivredriver.home.di;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.home.HomeInteractor;
import com.rsm.yuri.projecttaxilivredriver.home.HomeInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.home.HomePresenter;
import com.rsm.yuri.projecttaxilivredriver.home.HomePresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.home.HomeRepository;
import com.rsm.yuri.projecttaxilivredriver.home.HomeRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.home.models.AreasHelper;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 09/03/2018.
 */
@Module
public class HomeModule {

    private HomeView view;

    public HomeModule(HomeView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    HomeView providesHomeView(){
        return view;
    }

    @Provides
    @Singleton
    HomePresenter providesHomePresenter(EventBus eventBus, HomeView homeView, HomeInteractor homeInteractor){
        return new HomePresenterImpl(homeView, homeInteractor, eventBus);
    }

    @Provides
    @Singleton
    HomeInteractor providesHomeInteractor(HomeRepository homeRepository){
        return new HomeInteractorImpl(homeRepository);
    }

    @Provides
    @Singleton
    HomeRepository providesHomeRepository(FirebaseAPI helper, EventBus eventBus, AreasHelper areasHelper){
        return new HomeRepositoryImpl(helper, eventBus, areasHelper);
    }

    @Provides
    @Singleton
    AreasHelper providesAreasHelper(){
        return new AreasHelper();
    }

}
