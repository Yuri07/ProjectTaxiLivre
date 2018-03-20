package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class HomeInteractorImpl implements HomeInteractor {

    HomeRepository repository;

    public HomeInteractorImpl(HomeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateLocation(LatLng location) {
        repository.updateLocation(location);
    }

    @Override
    public void removeDriverFromArea() {
        repository.removeDriverFromArea();
    }
}
