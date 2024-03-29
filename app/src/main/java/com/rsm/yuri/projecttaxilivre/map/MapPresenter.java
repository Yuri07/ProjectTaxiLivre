package com.rsm.yuri.projecttaxilivre.map;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.entities.Rating;
import com.rsm.yuri.projecttaxilivre.map.entities.TravelRequest;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;
import com.rsm.yuri.projecttaxilivre.travelslist.entities.Travel;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapPresenter {

    void unsubscribe();
    void subscribe(LatLng location);
    void onCreate();
    void onDestroy();

    void updateMyLocation(LatLng location);

    void carRequest(NearDriver requestedDriver, TravelRequest travelRequest);

    void subscribeForResponseOfDriverRequested();
    void unsubscribeForResponseOfDriverRequested();
    public void deleteAckTravelRequest();

    void onEventMainThread(MapEvent event);

    void subscribeForMyDriverLocationUpdate(String driverEmail, String travelID);
    void unsubscribeForMyDriverLocationUpdate(String driverEmail, String travelID);

    void uploadMyRating(String emailDriver, Rating rating);

    void retrieveDataUser();
}
