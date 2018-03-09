package com.rsm.yuri.projecttaxilivredriver.home;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.home.events.MapHomeEvent;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class HomeRepositoryImpl implements HomeRepository {

    FirebaseAPI firebase;
    EventBus eventBus;

    public HomeRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void updateLocation(LatLng location) {
        firebase.updateMyLocation(location, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                Log.d("d", "HomeRepositoryImpl().updateLocation on Sucess()");
            }
            @Override
            public void onError(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    private void post(String error){
        MapHomeEvent event = new MapHomeEvent();
        event.setError(error);
        eventBus.post(event);
    }

}
