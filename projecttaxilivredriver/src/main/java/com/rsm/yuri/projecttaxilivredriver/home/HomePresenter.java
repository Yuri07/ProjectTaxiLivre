package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivredriver.home.events.MapHomeEvent;

/**
 * Created by yuri_ on 09/03/2018.
 */

public interface HomePresenter {

    void onCreate();
    void onDestroy();

    void updateLocation(LatLng location);
    void removeDriverFromArea();

    void onEventMainThread(MapHomeEvent event);

}
