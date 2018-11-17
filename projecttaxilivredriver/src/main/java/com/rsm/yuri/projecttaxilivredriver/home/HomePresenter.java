package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.home.events.MapHomeEvent;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

/**
 * Created by yuri_ on 09/03/2018.
 */

public interface HomePresenter {

    void onCreate();
    void onDestroy();

    void updateLocation(LatLng location);
    void removeDriverFromArea();

    void onEventMainThread(MapHomeEvent event);

    void uploadDriverDataToArea(NearDriver nearDriver);

    void acceptTravel(Travel travel);
}
