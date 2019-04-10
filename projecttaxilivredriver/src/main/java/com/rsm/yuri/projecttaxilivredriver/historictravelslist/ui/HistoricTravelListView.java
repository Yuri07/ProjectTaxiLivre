package com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui;

import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public interface HistoricTravelListView {

    void onHistoricTravelAdded(HistoricTravelItem travelItem);
    void onHistoricTravelChanged(Travel travel);
    void onHistoricTravelRemoved(Travel travel);
    void onHistoricTravelError(String error);

    void onUrlPhotoMapTravelRetrived(Travel travel);

}
