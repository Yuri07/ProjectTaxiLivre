package com.rsm.yuri.projecttaxilivredriver.historictravelslist;

import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public interface HistoricTravelsListRepository {

    void destroyHistoricTravelListListener();
    void subscribeForHistoricTravelListUpdates();
    void unSubscribeForHistoricTravelListUpdates();

    void changeUserConnectionStatus(int status);
    void getUrlPhotoMapTravel(Travel travel);

}
