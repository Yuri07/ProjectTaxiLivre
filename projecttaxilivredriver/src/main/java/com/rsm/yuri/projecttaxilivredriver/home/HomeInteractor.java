package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

/**
 * Created by yuri_ on 09/03/2018.
 */

public interface HomeInteractor {
    void updateLocation(LatLng latLng);
    void removeDriverFromArea();

    void uploadNearDriverData(NearDriver nearDriver);

    void acceptTravel(Travel travel);
}
