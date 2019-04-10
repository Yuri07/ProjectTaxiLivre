package com.rsm.yuri.projecttaxilivredriver.historictravelslist;

import com.rsm.yuri.projecttaxilivredriver.historictravelslist.events.HistoricTravelListEvent;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public interface HistoricTravelsListPresenter {

    void onPause();
    void onResume();
    void onCreate();
    void onDestroy();

    void onEventMainThread(HistoricTravelListEvent event);

    void getUrlPhotoMapFromTravel(Travel travel);

}
