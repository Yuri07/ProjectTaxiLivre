package com.rsm.yuri.projecttaxilivredriver.historictravelslist;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.events.HistoricTravelListEvent;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

public class HistoricTravelsListRepositoryImpl implements HistoricTravelsListRepository {

    FirebaseAPI firebase;
    EventBus eventBus;

    public HistoricTravelsListRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void subscribeForHistoricTravelListUpdates() {
        firebase.subscribeForHistoricTravelsListUpdates(new FirebaseEventListenerCallback() {//addChildEventListener
            @Override public void onChildAdded(DataSnapshot dataSnapshot) {
                /*String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();*/
                HistoricTravelItem travelItem = dataSnapshot.getValue(HistoricTravelItem.class);

                post(HistoricTravelListEvent.onHistoricTravelAdded, travelItem);
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot){
                /*String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();
                User user = new User();
                user.setEmail(email);
                user.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatChanged, user);*/
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot){

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                /*String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();
                User user = new User();
                user.setEmail(email);
                user.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatRemoved, user);*/
            }

            @Override public void onCancelled(DatabaseError error) {
                post(HistoricTravelListEvent.ERROR_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void unSubscribeForHistoricTravelListUpdates() {
        firebase.unSubscribeForHistoricTravelsListUpdates();
    }

    @Override
    public void changeUserConnectionStatus(int status) {

    }

    @Override
    public void getUrlPhotoMapTravel(final Travel travel) {
        firebase.getUrlPhotoMapTravel(travel.getTravelId(), new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                String url = (String) dataSnapshot.getValue();
                Travel travelUrlUpdated = travel;
                travelUrlUpdated.setUrlPhotoMap(url);
                post(HistoricTravelListEvent.onUrlPhotoMapTravelRetrived, travelUrlUpdated);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(HistoricTravelListEvent.ERROR_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void destroyHistoricTravelListListener() {
        firebase.destroyHistoricTravelsListener();
    }

    private void post(int type, HistoricTravelItem travelItem){
        post(type, travelItem, null,null);
    }

    private void post(int type, Travel urlUpdatedTravel){
        post(type, null, urlUpdatedTravel, null);
    }

    private void post(int type, String error){
        post(type, null, null, error);
    }

    private void post(int type, HistoricTravelItem travelItem, Travel travel,String error){
        HistoricTravelListEvent event = new HistoricTravelListEvent();
        event.setEventType(type);
        event.setError(error);
        event.setTravelItem(travelItem);
        event.setTravel(travel);
        eventBus.post(event);
    }

}
