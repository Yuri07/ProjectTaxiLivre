package com.rsm.yuri.projecttaxilivredriver.historictravelslist;

import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.events.HistoricTravelListEvent;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.HistoricTravelListView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

import org.greenrobot.eventbus.Subscribe;

public class HistoricTravelsListPresenterImpl implements HistoricTravelsListPresenter {

    EventBus eventBus;
    HistoricTravelListView historicTravelsListView;
    HistoricTravelsListInteractor historicTravelsListInteractor;
    //HistoricChatsListSessionInteractor sessionInteractor;


    public HistoricTravelsListPresenterImpl(EventBus eventBus, HistoricTravelListView historicTravelsListView, HistoricTravelsListInteractor historicTravelsListInteractor) {
        this.eventBus = eventBus;
        this.historicTravelsListView = historicTravelsListView;
        this.historicTravelsListInteractor = historicTravelsListInteractor;
    }

    @Override
    public void onPause() {
        historicTravelsListInteractor.unSubscribeForHistoricTravelEvents();
    }

    @Override
    public void onResume() {
        historicTravelsListInteractor.subscribeForHistoricTravelEvents();
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        historicTravelsListInteractor.destroyHistoricTravelListListener();
        historicTravelsListView = null;
    }

    @Override
    public void getUrlPhotoMapFromTravel(Travel travel) {
        historicTravelsListInteractor.getUrlPhotoMapTravel(travel);
    }

    @Override
    @Subscribe
    public void onEventMainThread(HistoricTravelListEvent event) {
        //Travel travel = event.getTravel();
        if (this.historicTravelsListView!= null) {
            String error = event.getError();
            if (error != null) {
                historicTravelsListView.onHistoricTravelError(error);
            }else {
                switch (event.getEventType()) {
                    case HistoricTravelListEvent.onHistoricTravelAdded:
                        HistoricTravelItem travelItem = event.getTravelItem();
                        Log.d("d", "event.type = historictravelAdded"+travelItem.getRequesterEmail());
                        onHistoricTravelAdded(travelItem);
                        break;
                    case HistoricTravelListEvent.onHistoricTravelChanged:
                        /*Log.d("d", "event.type = historictraveltChanged"+travel.getRequesterEmail());
                        onHistoricTravelChanged(travel);*/
                        break;
                    case HistoricTravelListEvent.onHistoricTravelRemoved:
                        /*Log.d("d", "event.type = historictravelRemoved"+travel.getRequesterEmail());
                        onHistoricTravelRemoved(travel);*/
                        break;
                    case HistoricTravelListEvent.onUrlPhotoMapTravelRetrived:
                        /*Travel travel = event.getTravel();
                        Log.d("d", "event.type = historictravelRetrived"+travel.getRequesterEmail());
                        onUrlPhotoMapTravelRetrived(travel);*/
                        break;
                }
            }
        }
    }

    private void onUrlPhotoMapTravelRetrived(Travel travel) {
        if (historicTravelsListView != null) {
            historicTravelsListView.onUrlPhotoMapTravelRetrived(travel);
        }
    }

    private void onHistoricTravelRemoved(Travel travel) {
        if (historicTravelsListView != null) {
            historicTravelsListView.onHistoricTravelRemoved(travel);
        }
    }

    private void onHistoricTravelChanged(Travel travel) {
        if (historicTravelsListView != null) {
            historicTravelsListView.onHistoricTravelChanged(travel);
        }
    }

    private void onHistoricTravelAdded(HistoricTravelItem travelItem) {
        if (historicTravelsListView != null) {
            historicTravelsListView.onHistoricTravelAdded(travelItem);
        }
    }


}
