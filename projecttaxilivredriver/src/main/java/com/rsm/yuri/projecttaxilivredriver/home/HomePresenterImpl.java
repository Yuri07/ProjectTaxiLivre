package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivredriver.home.events.MapHomeEvent;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class HomePresenterImpl implements HomePresenter {

    HomeView view;
    HomeInteractor interactor;
    EventBus eventBus;

    public HomePresenterImpl(HomeView view, HomeInteractor interactor, EventBus eventBus) {
        this.view = view;
        this.interactor = interactor;
        this.eventBus = eventBus;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void updateLocation(LatLng location) {
        interactor.updateLocation(location);
    }

    @Override
    public void removeDriverFromArea() {
        interactor.removeDriverFromArea();
    }

    @Override
    @Subscribe
    public void onEventMainThread(MapHomeEvent event) {
        String error = event.getError();
        if (error != null) {
            view.onLocationReadingError(error);
        }
    }


}
