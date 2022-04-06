package com.rsm.yuri.projecttaxilivredriver.home;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class HomeInteractorImpl implements HomeInteractor {

    HomeRepository repository;

    public HomeInteractorImpl(HomeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void updateLocation(LatLng latLng) {
        repository.updateLocation(latLng);
    }

    @Override
    public void removeDriverFromArea() {
        repository.removeDriverFromArea();
    }

    @Override
    public void uploadNearDriverData(NearDriver nearDriver) {
        repository.uploadNearDriverData(nearDriver);
    }

    @Override
    public void acceptTravel(Travel travel) {
        repository.acceptTravel(travel);
    }

    @Override
    public void notifyRequesterTravelNotAccepted(String requesterEmail) {
        repository.notifyRequesterTravelNotAccepted(requesterEmail);
    }

    @Override
    public void updateLocationForTravel(LatLng location, String requesterEmail, String travelID) {
        repository.updateLocationForTravel(location, requesterEmail, travelID);
    }

    @Override
    public void initiateTravel(String emailRequester, String travelID) {
        repository.initiateTravel(emailRequester, travelID);
    }

    @Override
    public void terminateTravel(String emailRequester, String travelID) {
        repository.terminateTravel(emailRequester, travelID);
    }

    @Override
    public void uploadUserRating(String userEmail, Rating rating) {
        repository.uploadUserRating(userEmail, rating);
    }

    @Override
    public void saveCity(String cidade) {
        repository.saveCity(cidade);
    }

    @Override
    public void retrieveDataUser() {
        repository.retrieveDataUser();
    }
}
