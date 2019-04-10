package com.rsm.yuri.projecttaxilivredriver.historictravelslist.events;

import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public class HistoricTravelListEvent {

    private HistoricTravelItem travelItem;
    private Travel travel;
    private int eventType;
    private String error;

    public final static int onHistoricTravelAdded = 0;
    public final static int onHistoricTravelChanged = 1;
    public final static int onHistoricTravelRemoved = 2;
    public final static int onUrlPhotoMapTravelRetrived = 3;
    public final static int ERROR_EVENT = 4;


    public HistoricTravelItem getTravelItem() {
        return travelItem;
    }

    public void setTravelItem(HistoricTravelItem travelItem) {
        this.travelItem = travelItem;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
