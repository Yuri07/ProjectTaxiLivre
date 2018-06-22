package com.rsm.yuri.projecttaxilivre.map;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapPresenter {

    void unsubscribe();
    void subscribe(LatLng location);
    void onCreate();
    void onDestroy();

    void updateMyLocation(LatLng location);

    void carRequest(NearDriver requestDriver);

    void onEventMainThread(MapEvent event);

}
