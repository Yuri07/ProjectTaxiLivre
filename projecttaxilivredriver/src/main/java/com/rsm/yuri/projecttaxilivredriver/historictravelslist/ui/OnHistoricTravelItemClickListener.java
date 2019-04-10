package com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui;

import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public interface OnHistoricTravelItemClickListener {

    void onItemClick(HistoricTravelItem travelItem);
    void onItemLongClick(HistoricTravelItem travelItem);

}
