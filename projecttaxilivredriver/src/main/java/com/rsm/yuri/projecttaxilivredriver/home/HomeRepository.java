package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;

/**
 * Created by yuri_ on 09/03/2018.
 */

public interface HomeRepository {
    void updateLocation(LatLng latLng);
    void removeDriverFromArea();

    void uploadNearDriverData(NearDriver nearDriver);
}
