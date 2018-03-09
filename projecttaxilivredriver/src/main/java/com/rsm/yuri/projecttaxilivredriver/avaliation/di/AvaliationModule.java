package com.rsm.yuri.projecttaxilivredriver.avaliation.di;

import com.rsm.yuri.projecttaxilivredriver.avaliation.AvaliationInteractor;
import com.rsm.yuri.projecttaxilivredriver.avaliation.AvaliationInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.avaliation.AvaliationPresenter;
import com.rsm.yuri.projecttaxilivredriver.avaliation.AvaliationPresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.avaliation.AvaliationRepository;
import com.rsm.yuri.projecttaxilivredriver.avaliation.AvaliationRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationView;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.adapters.AvaliationsListAdapter;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 07/03/2018.
 */
@Module
public class AvaliationModule {

    AvaliationView view;

    public AvaliationModule(AvaliationView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    AvaliationView providesAvaliationView() {
        return this.view;
    }

    @Provides
    @Singleton
    AvaliationPresenter providesAvaliationPresenter(EventBus eventBus, AvaliationView view, AvaliationInteractor listInteractor) {
        return new AvaliationPresenterImpl(eventBus, view, listInteractor);
    }

    @Provides @Singleton
    AvaliationInteractor providesAvaliationInteractor(AvaliationRepository repository) {
        return new AvaliationInteractorImpl(repository);
    }

    @Provides @Singleton
    AvaliationRepository providesAvaliationRepository(FirebaseAPI firebase, EventBus eventBus) {
        return new AvaliationRepositoryImpl(firebase, eventBus);
    }

    @Provides @Singleton
    AvaliationsListAdapter providesAvaliationAdapter(List<Rating> avaliationList, ImageLoader imageLoader) {
        return new AvaliationsListAdapter(avaliationList, imageLoader);
    }

    @Provides @Singleton
    List<Rating> providesAvaliationsList() {
        return new ArrayList<Rating>();
    }

}
