package com.rsm.yuri.projecttaxilivre.map;

import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;
import com.rsm.yuri.projecttaxilivre.map.ui.MapView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 22/01/2018.
 */

public class MapPresenterImpl implements MapPresenter {
    EventBus eventBus;
    MapView mapView;
    MapInteractor mapInteractor;

    public MapPresenterImpl(EventBus eventBus, MapView mapView, MapInteractor mapInteractor) {
        this.eventBus = eventBus;
        this.mapView = mapView;
        this.mapInteractor = mapInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mapInteractor.destroyDriversListener();
        eventBus.unregister(this);
        this.mapView = null;

    }

    @Override
    public void onResume() {
        mapInteractor.subscribeForDriversUpdates();
    }

    @Override
    public void onPause() {
        mapInteractor.unSubscribeForDriversUpdates();
    }

    @Override
    @Subscribe
    public void onEventMainThread(MapEvent event) {
        String error = event.getError();
        if (error != null) {
            mapView.onDriverError(error);
        } else {
            Driver driver = event.getDriver();
            switch (event.getEventType()) {
                case MapEvent.onDriverAdded:
                    onDriverAdded(driver);
                    break;
                case MapEvent.onDriverMoved:
                    onDriverMoved(driver);
                    break;
                case MapEvent.onDriverRemoved:
                    onDriverRemoved(driver);
                    break;
            }
        }

    }

    private void onDriverAdded(Driver driver) {
        if(mapView!=null){
            mapView.onDriverAdded(driver);
        }
    }

    private void onDriverMoved(Driver driver) {
        if(mapView!=null){
            mapView.onDriverMoved(driver);
        }
    }

    private void onDriverRemoved(Driver driver) {
        if(mapView!=null){
            mapView.onDriverRemoved(driver);
        }
    }





}
