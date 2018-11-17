package com.rsm.yuri.projecttaxilivre.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.entities.TravelRequest;
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
    public void subscribe(LatLng location) {
        //Log.d("d", "Presenter.subscribe()");
        mapInteractor.subscribeForDriversUpdates(location);
    }

    @Override
    public void unsubscribe() {
        mapInteractor.unSubscribeForDriversUpdates();
    }

    @Override
    public void updateMyLocation(LatLng location) {
        mapInteractor.updateMyLocation(location);
    }

    @Override
    public void carRequest(NearDriver requestDriver, TravelRequest travelRequest){
        mapInteractor.requestDriver(requestDriver, travelRequest);
    }

    @Override
    public void subscribeForResponseOfDriverRequested() {
        mapInteractor.subscribeForResponseOfDriverRequested();
    }

    @Override
    public void unsubscribeForResponseOfDriverRequested() {
        mapInteractor.unsubscribeForResponseOfDriverRequested();
    }

    @Override
    @Subscribe
    public void onEventMainThread(MapEvent event) {
        String error = event.getError();
        if (error != null) {
            mapView.onDriverError(error);
        } else {
            String travelAck = event.getTravelAck();
            if(travelAck!=null) {
                mapView.onTravelAckReceived(travelAck);
            }else{
                NearDriver nearDriver = event.getNearDriver();
                switch (event.getEventType()) {
                    case MapEvent.onDriverAdded:
                        onDriverAdded(nearDriver);
                        break;
                    case MapEvent.onDriverMoved:
                        onDriverMoved(nearDriver);
                        break;
                    case MapEvent.onDriverRemoved:
                        onDriverRemoved(nearDriver);
                        break;
                }
            }
        }

    }

    private void onDriverAdded(NearDriver nearDriver) {
        if(mapView!=null){
            mapView.onDriverAdded(nearDriver);
        }
    }

    private void onDriverMoved(NearDriver nearDriver) {
        if(mapView!=null){
            mapView.onDriverMoved(nearDriver);
        }
    }

    private void onDriverRemoved(NearDriver nearDriver) {
        if(mapView!=null){
            mapView.onDriverRemoved(nearDriver);
        }
    }





}
