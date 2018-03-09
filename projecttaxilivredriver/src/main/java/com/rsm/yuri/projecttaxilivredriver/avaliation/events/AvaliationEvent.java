package com.rsm.yuri.projecttaxilivredriver.avaliation.events;

import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;

/**
 * Created by yuri_ on 07/03/2018.
 */

public class AvaliationEvent {

    private Rating rating;
    private int eventType;
    private String error;

    public final static int READ_EVENT = 1;
    public final static int ERROR_EVENT = 0;

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
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
