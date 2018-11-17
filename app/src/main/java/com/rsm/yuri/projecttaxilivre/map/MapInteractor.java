package com.rsm.yuri.projecttaxilivre.map;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.entities.TravelRequest;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapInteractor {

    void subscribeForDriversUpdates(LatLng location);
    void unSubscribeForDriversUpdates();
    void destroyDriversListener();

    void updateMyLocation(LatLng location);

    void requestDriver(NearDriver requestedDriver, TravelRequest travelRequest);

    void subscribeForResponseOfDriverRequested();
    void unsubscribeForResponseOfDriverRequested();

    /*void addNearDrivers();////nao tem essas funcoes disponiveis para o usuario na tela do mapfragment
    void removeNearDrivers();*/

}
