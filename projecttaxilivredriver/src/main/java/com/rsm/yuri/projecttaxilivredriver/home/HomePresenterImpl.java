package com.rsm.yuri.projecttaxilivredriver.home;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.home.events.MapHomeEvent;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class HomePresenterImpl implements HomePresenter {

    HomeView view;
    HomeInteractor interactor;
    EventBus eventBus;

    public HomePresenterImpl(HomeView view, HomeInteractor interactor, EventBus eventBus) {
        this.view = view;
        this.interactor = interactor;
        this.eventBus = eventBus;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void uploadDriverDataToArea(NearDriver nearDriver) {
        interactor.uploadNearDriverData(nearDriver);
    }

    @Override
    public void acceptTravel(Travel travel) {
        interactor.acceptTravel(travel);
    }

    @Override
    public void notifyRequesterTravelNotAccepted(String requesterEmail) {
        interactor.notifyRequesterTravelNotAccepted(requesterEmail);
    }

    @Override
    public void updateLocation(LatLng latLng) {
        interactor.updateLocation(latLng);
    }

    @Override
    public void updateLocationForTravel(LatLng location, String requesterEmail, String travelID) {
        interactor.updateLocationForTravel(location, requesterEmail, travelID);
    }

    @Override
    public void removeDriverFromArea() {
        interactor.removeDriverFromArea();
    }

    @Override
    public void initiateTravel(String emailRequester, String travelID) {
        interactor.initiateTravel(emailRequester,travelID);
    }

    @Override
    public void terminateTravel(String emailRequester, String travelID) {
        interactor.terminateTravel(emailRequester, travelID);
    }

    @Override
    public void uploadUserRating(String userEmail, Rating rating) {
        interactor.uploadUserRating(userEmail, rating);
    }

    @Override
    public void saveCity(String cidade) {
        interactor.saveCity(cidade);
    }

    @Override
    @Subscribe
    public void onEventMainThread(MapHomeEvent event) {
        Log.d("d", "HomePresenterImpl.onEventMainThread");
        String error = event.getError();
        if (error != null) {
            Log.d("d", "HomePresenterImpl.onEventMainThread error != null");
            view.onLocationReadingError(error);
        }else{
            Log.d("d", "HomePresenterImpl.onEventMainThread error == null");
            if(event.getEventType()==MapHomeEvent.ON_TRAVEL_CREATED){
                Log.d("d", "HomePresenterImpl.onEventMainThread eventType == ON_TRAVEL_CREATED");
                view.onTravelCreated(event.getNewTravelID());
            }
        }
    }


}
