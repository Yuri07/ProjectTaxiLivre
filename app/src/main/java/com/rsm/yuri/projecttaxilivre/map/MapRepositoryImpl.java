package com.rsm.yuri.projecttaxilivre.map;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;
import com.rsm.yuri.projecttaxilivre.map.models.AreasHelper;
import com.rsm.yuri.projecttaxilivre.map.models.GroupAreas;

/**
 * Created by yuri_ on 22/01/2018.
 */

public class MapRepositoryImpl implements MapRepository {

    private FirebaseAPI firebase;
    private EventBus eventBus;

    public MapRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void subscribeForDriversEvents() {
        Log.d("d", "MapRepository.subscribe()");
        firebase.subscribe(new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                NearDriver nearDriver = dataSnapshot.getValue(NearDriver.class);
                Log.d("d", "post(addNearDriver, nearDriver); nearDriver.getemail(): " + nearDriver.getEmail());
                /*Log.d("d", "NearDriver.getemail(): " + nearDriver.getEmail());
                Log.d("d", "NearDriver.getLatitude(): " + nearDriver.getLatitude());
                Log.d("d", "NearDriver.getLongitude(): " + nearDriver.getLongitude());*/
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
                post(MapEvent.onDriverRemoved, nearDriver);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void unSubscribeForDriversEvents() {
        firebase.unsubscribe();

    }

    @Override
    public void destroyDriversListener() {

    }

    @Override
    public void addNearDrivers() {
        //GroupAreas groupAreas = areasHelper.getGroupAreas(-3.740146, -38.606009 );
        //NearDriver[] nearDrivers = firebase.getNearDrivers(groupAreas, -3.740146, -38.606009 );

    }

    @Override
    public void removeNearDrivers() {

    }

    private void post(int type, NearDriver nearDriver){
        post(type, nearDriver, null);
    }

    private void post(String error){
        post(0, null, error);
    }

    private void post(int type, NearDriver nearDriver, String error){
        MapEvent event = new MapEvent();
        event.setEventType(type);
        event.setError(error);
        event.setNearDriver(nearDriver);
        eventBus.post(event);
    }

}
