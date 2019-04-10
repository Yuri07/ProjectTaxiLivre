package com.rsm.yuri.projecttaxilivredriver.historictravelslist;

import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public interface HistoricTravelsListInteractor {

    void subscribeForHistoricTravelEvents();
    void unSubscribeForHistoricTravelEvents();
    void destroyHistoricTravelListListener();

    void getUrlPhotoMapTravel(Travel travel);

}
