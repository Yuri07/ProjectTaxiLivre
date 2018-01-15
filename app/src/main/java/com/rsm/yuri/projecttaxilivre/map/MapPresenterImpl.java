package com.rsm.yuri.projecttaxilivre.map;

import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.login.events.LoginEvent;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;
import com.rsm.yuri.projecttaxilivre.map.ui.MapView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MapPresenterImpl implements MapPresenter {

    EventBus eventBus;
    MapView mapView;
    MapInteractor mapInteractor;
    SessionInteractor sessionInteractor;

    public MapPresenterImpl(EventBus eventBus, MapView mapView, MapInteractor mapInteractor, SessionInteractor sessionInteractor) {
        this.eventBus = eventBus;
        this.mapView = mapView;
        this.mapInteractor = mapInteractor;
        this.sessionInteractor = sessionInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        mapView = null;
        eventBus.unregister(this);

    }

    @Override
    @Subscribe
    public void onEventMainThread(MapEvent event) {
        switch (event.getEventType()) {
            case MapEvent.onSuccessToRecoverSession:
                onSuccessToRecoverSession(event.getLoggedUserEmail());
                break;
            case LoginEvent.onFailedToRecoverSession:
                onFailedToRecoverSession();
                break;
        }
    }

    private void onSuccessToRecoverSession(String email) {
        if (mapView != null) {
            mapView.setUserEmail(email);
            mapView.setUIVisibility(true);
        }
    }

    private void onFailedToRecoverSession() {
        if (mapView != null) {
            mapView.navigateToLoginScreen();
        }
    }

    @Override
    public void logout() {
        sessionInteractor.logout();
    }

    @Override
    public void checkForSession() {
        sessionInteractor.checkForSession();
    }
}
