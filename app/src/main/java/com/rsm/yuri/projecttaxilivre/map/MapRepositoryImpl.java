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
import com.rsm.yuri.projecttaxilivre.map.entities.TravelRequest;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;
import com.rsm.yuri.projecttaxilivre.map.models.Area;
import com.rsm.yuri.projecttaxilivre.map.models.AreasHelper;
import com.rsm.yuri.projecttaxilivre.map.models.GroupAreas;

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
        groupAreas = areasHelper.getGroupAreas(location.latitude, location.longitude);
        firebase.subscribeForNearDriversUpdate(groupAreas, new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                NearDriver nearDriver = dataSnapshot.getValue(NearDriver.class);
                post(MapEvent.onDriverAdded, nearDriver);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {
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
        firebase.subscribeForResponseOfDriverRequested( new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                /*long status = (long) dataSnapshot.getValue();
                Log.d("d", "subscribeForStatusReceiverUpdates onchildAdded status((long) dataSnapshot.getValue()) = " + status);

                post(ChatEvent.READ_STATUS_RECEIVER_EVENT, status);*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {
                String travelAck = (String) dataSnapshot.getValue();
                //Log.d("d", "subscribeForStatusReceiverUpdates onchildChanged status((long) dataSnapshot.getValue()) = " + status);

                //if(travelAck!=null)
                    post(MapEvent.onTravelAckReceived, travelAck);
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

    private void post(int type, NearDriver nearDriver){
        post(type, nearDriver, null, null);
    }

    private void post(int type, String travelAck){
        post(type, null, travelAck, null);
    }

    private void post(String error){
        post(0, null, null,error);
    }

    private void post(int type, NearDriver nearDriver, String travelAck, String error){
        MapEvent event = new MapEvent();
        event.setEventType(type);
        event.setNearDriver(nearDriver);
        event.setTravelAck(travelAck);
        event.setError(error);

        eventBus.post(event);
    }

}
