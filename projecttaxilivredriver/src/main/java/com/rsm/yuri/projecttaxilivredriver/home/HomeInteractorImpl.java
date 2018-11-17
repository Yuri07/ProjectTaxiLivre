package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class HomeInteractorImpl implements HomeInteractor {

    HomeRepository repository;

    public HomeInteractorImpl(HomeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateLocation(LatLng latLng) {
        repository.updateLocation(latLng);
    }

    @Override
    public void removeDriverFromArea() {
        repository.removeDriverFromArea();
    }

    @Override
    public void uploadNearDriverData(NearDriver nearDriver) {
        repository.uploadNearDriverData(nearDriver);
    }

    @Override
    public void acceptTravel(Travel travel) {
        repository.acceptTravel(travel);
    }
}
