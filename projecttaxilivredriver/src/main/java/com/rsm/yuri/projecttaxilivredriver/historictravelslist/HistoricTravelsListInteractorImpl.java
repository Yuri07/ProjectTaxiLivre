package com.rsm.yuri.projecttaxilivredriver.historictravelslist;

import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public class HistoricTravelsListInteractorImpl implements HistoricTravelsListInteractor {

    HistoricTravelsListRepository historicTravelsListRepository;

    public HistoricTravelsListInteractorImpl(HistoricTravelsListRepository historicTravelsListRepository) {
        this.historicTravelsListRepository = historicTravelsListRepository;
    }

    @Override
    public void subscribeForHistoricTravelEvents() {
        historicTravelsListRepository.subscribeForHistoricTravelListUpdates();
    }

    @Override
    public void unSubscribeForHistoricTravelEvents() {
        historicTravelsListRepository.unSubscribeForHistoricTravelListUpdates();
    }

    @Override
    public void destroyHistoricTravelListListener() {
        historicTravelsListRepository.destroyHistoricTravelListListener();
    }

    @Override
    public void getUrlPhotoMapTravel(Travel travel) {
        historicTravelsListRepository.getUrlPhotoMapTravel(travel);
    }
}
