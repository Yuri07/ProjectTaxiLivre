package com.rsm.yuri.projecttaxilivre.map;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.entities.Rating;
import com.rsm.yuri.projecttaxilivre.map.entities.TravelRequest;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;
import com.rsm.yuri.projecttaxilivre.map.models.Area;
import com.rsm.yuri.projecttaxilivre.map.models.AreasFortalezaHelper;
import com.rsm.yuri.projecttaxilivre.map.models.AreasHelper;
import com.rsm.yuri.projecttaxilivre.map.models.GroupAreas;
import com.rsm.yuri.projecttaxilivre.travelslist.entities.Travel;

/**
 * Created by yuri_ on 22/01/2018.
 */

public class MapRepositoryImpl implements MapRepository {

    private FirebaseAPI firebase;
    private EventBus eventBus;
    GroupAreas groupAreas;
    private AreasHelper areasHelper;

    public MapRepositoryImpl(FirebaseAPI firebase, EventBus eventBus, AreasHelper areasHelper) {
        this.firebase = firebase;
        this.eventBus = eventBus;
        this.areasHelper = areasHelper;
    }

    @Override
    public void subscribeForDriversEvents(LatLng location) {
        Log.d("d", "MapRepositoryImpl - subscribeForDriversEvents()");
        groupAreas = areasHelper.getGroupAreas(location.latitude, location.longitude);
        firebase.subscribeForNearDriversUpdate(groupAreas, new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Log.d("d", "MapRepositoryImpl - subscribeForDriversEvents() - onChildAdded()");
                NearDriver nearDriver = dataSnapshot.getValue(NearDriver.class);
                post(MapEvent.onDriverAdded, nearDriver);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {
                Log.d("d", "MapRepositoryImpl - subscribeForDriversEvents() - onChildChanged()");
                NearDriver nearDriver = dataSnapshot.getValue(NearDriver.class);
                post(MapEvent.onDriverMoved, nearDriver);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                NearDriver nearDriver = dataSnapshot.getValue(NearDriver.class);
                post(MapEvent.onDriverRemoved, nearDriver);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot) {
                NearDriver nearDriver = dataSnapshot.getValue(NearDriver.class);
                post(MapEvent.onDriverMoved, nearDriver);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void unSubscribeForDriversEvents() {
        firebase.unsubscribeForNearDriversUpdates();
    }

    @Override
    public void destroyDriversListener() {
        firebase.destroyDriversListener();
    }

    @Override
    public void updateMyLocation(LatLng location) {
        firebase.updateMyLocation(location, new FirebaseActionListenerCallback(){

            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void requestDriver(NearDriver requestedDriver, TravelRequest travelRequest){
        GroupAreas groupAreasRequestedDriver = areasHelper.getGroupAreas(requestedDriver.getLatitude(), requestedDriver.getLongitude());
        Area areaRequestedDriver = groupAreasRequestedDriver.getMainArea();
        firebase.setTravelRequest(travelRequest, requestedDriver.getEmail(), areaRequestedDriver);
    }

    @Override
    public void subscribeForResponseOfDriverRequested() {
        Log.d("d", "MapRepository - subscribeForResponseofDriverRequested()");

        firebase.subscribeForResponseOfDriverRequested( new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {

                Log.d("d", "MapRepository - subscribeForStatusReceiverUpdates() onchildAdded()" );

                //firebase.deleteAckTravelRequest();

                /*String idTravel = dataSnapshot.getKey();
                String email = (String) dataSnapshot.getValue();
                Log.d("d", "MapRepositoryImpl - subscribeForStatusReceiverUpdates onchildAdded idTravel dataSnapshot.getValue()) = " + idTravel);*/
                /*long status = (long) dataSnapshot.getValue();
                Log.d("d", "subscribeForStatusReceiverUpdates onchildAdded status((long) dataSnapshot.getValue()) = " + status);*/

                //post(MapEvent.onTravelAckReceived, idTravel);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {
                String idTravel = (String) dataSnapshot.getValue();
                //post(MapEvent.onTravelAckReceived, idTravel);

                //String travelAck = (String) dataSnapshot.getValue();
                Log.d("d", "MapRepository - subscribeForResponseofDriverRequested() " +
                        "onchildChanged idTravel dataSnapshot.getValue()) = " + idTravel);


                if(idTravel!=null && !idTravel.equals(""))
                    post(MapEvent.onTravelAckReceived, idTravel);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void unsubscribeForResponseOfDriverRequested() {
        firebase.unSubscribeForResponseOfDriverRequested();
    }

    @Override
    public void deleteAckTravelRequest(){
        firebase.deleteAckTravelRequest();
    }

    @Override
    public void subscribeForMyDriverLocationUpdate(String driverEmail, String travelID) {
        Log.d("d", "MapRepository - subscribeForMyDriverLocationUpdate");
        firebase.subscribeForMyDriverLocationUpdate(driverEmail, travelID,new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {

                Log.d("d", "MapRepository - subscribeForMyDriverLocationUpdate - onChilAdded ");

                /*Travel travel = dataSnapshot.getValue(Travel.class);
                Log.d("d", "MapRepository - subscribeForMyDriverLocationUpdate - " +
                        "onChildAdded - myDriver.getLat(): "+travel.getLatDriver());
                LatLng locationOfMyDriver = new LatLng(travel.getLatDriver(),travel.getLongDriver());
                post(MapEvent.onMyDriverMoved, locationOfMyDriver);*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {
                Travel travel = dataSnapshot.getValue(Travel.class);
                Log.d("d", "MapRepository - subscribeForMyDriverLocationUpdate - " +
                        "onChilChanged - myDriver.getLat(): "+travel.getLatDriver());
                LatLng locationOfMyDriver = new LatLng(travel.getLatDriver(),travel.getLongDriver());
                post(MapEvent.onMyDriverMoved, locationOfMyDriver);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void unsubscribeForMyDriverLocationUpdate(String driverEmail, String travelID) {
        firebase.unsubscribeForMyDriverLocationUpdate();
    }

    @Override
    public void uploadMyRating(String emailDriver, Rating rating) {
        firebase.uploadMyRating(emailDriver, rating);
    }

    private void post(int type, NearDriver nearDriver){
        post(type, nearDriver, null, null, null);
    }

    private void post(int type, String travelAck){
        post(type, null, travelAck, null, null);
    }

    private void post(int type, LatLng locationOfMyDriver){
        post(type, null, null, locationOfMyDriver, null);
    }

    private void post(String error){
        post(-1, null, null, null,error);
    }

    private void post(int type, NearDriver nearDriver, String travelAck, LatLng locationOfMyDriver, String error){
        MapEvent event = new MapEvent();
        event.setEventType(type);
        event.setNearDriver(nearDriver);
        event.setTravelAck(travelAck);
        event.setLocationOfMyDriver(locationOfMyDriver);
        event.setError(error);

        eventBus.post(event);
    }

}
