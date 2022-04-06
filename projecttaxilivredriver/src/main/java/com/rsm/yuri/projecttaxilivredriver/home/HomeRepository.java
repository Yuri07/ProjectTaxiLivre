package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

/**
 * Created by yuri_ on 09/03/2018.
 */

public interface HomeRepository {
    void updateLocation(LatLng latLng);
    void removeDriverFromArea();

    void uploadNearDriverData(NearDriver nearDriver);

    void acceptTravel(Travel travel);
    void notifyRequesterTravelNotAccepted(String requesterEmail);

    void updateLocationForTravel(LatLng location, String requesterEmail, String travelID);

    void initiateTravel(String emailRequester, String travelID);
    void terminateTravel(String emailRequester, String travelID);

    void uploadUserRating(String userEmail, Rating rating);

    void saveCity(String cidade);

    void retrieveDataUser();
}
