package com.rsm.yuri.projecttaxilivre.map;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.entities.TravelRequest;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapRepository {

    void subscribeForDriversEvents(LatLng location);
    void unSubscribeForDriversEvents();
    void destroyDriversListener();

    void updateMyLocation(LatLng location);

    void requestDriver(NearDriver requestedDriver, TravelRequest travelRequest);

    void subscribeForResponseOfDriverRequested();
    void unsubscribeForResponseOfDriverRequested();

    //void addNearDrivers();//nao tem essas funções para o usuario na tela do mapfragment.
    //void removeNearDrivers();

}
