package com.rsm.yuri.projecttaxilivre.map;

import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
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

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    /*@Override
    @Subscribe
    public void onEventMainThread(PhotoMapEvent event) {
        if (this.view != null) {
            String error = event.getError();
            if (error != null) {
                view.onPhotosError(error);
            } else {
                if (event.getType() == PhotoListEvent.READ_EVENT) {
                    view.addPhoto(event.getPhoto());
                } else if (event.getType() == PhotoListEvent.DELETE_EVENT) {
                    view.removePhoto(event.getPhoto());
                }
            }
        }
    }*/

}
