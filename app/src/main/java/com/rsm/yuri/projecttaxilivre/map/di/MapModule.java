package com.rsm.yuri.projecttaxilivre.map.di;

import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.MapInteractor;
import com.rsm.yuri.projecttaxilivre.map.MapInteractorImpl;
import com.rsm.yuri.projecttaxilivre.map.MapPresenter;
import com.rsm.yuri.projecttaxilivre.map.MapPresenterImpl;
import com.rsm.yuri.projecttaxilivre.map.MapRepository;
import com.rsm.yuri.projecttaxilivre.map.MapRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.map.ui.MapView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 22/01/2018.
 */
@Module
public class MapModule {

    MapView mapView;

    public MapModule(MapView mapView){
        this.mapView = mapView;
    }

    @Provides
    @Singleton
    MapView providesMapView(){
        return  mapView;
    }

    @Provides
    @Singleton
    MapPresenter providesMapPresenter(EventBus eventBus, MapView mapView, MapInteractor mapInteractor){
        return new MapPresenterImpl(eventBus, mapView, mapInteractor);
    }

    @Provides
    @Singleton
    MapInteractor providesMapInteractor(MapRepository mapRepository){
        return new MapInteractorImpl(mapRepository);
    }

    @Provides
    @Singleton
    MapRepository providesMapRepository(FirebaseAPI firebase, EventBus eventBus){
        return new MapRepositoryImpl(firebase, eventBus);
    }

}
