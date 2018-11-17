package com.rsm.yuri.projecttaxilivredriver.home;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.home.events.MapHomeEvent;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.home.models.AreasHelper;
import com.rsm.yuri.projecttaxilivredriver.home.models.GroupAreas;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

/**
 * Created by yuri_ on 09/03/2018.
 */

public class HomeRepositoryImpl implements HomeRepository {

    FirebaseAPI firebase;
    EventBus eventBus;
    GroupAreas groupAreas;
    private AreasHelper areasHelper;

    public HomeRepositoryImpl(FirebaseAPI firebase, EventBus eventBus/*, GroupAreas groupAreas*/, AreasHelper areasHelper) {
        this.firebase = firebase;
        this.eventBus = eventBus;
        this.groupAreas = null;
        //this.groupAreas = groupAreas;
        this.areasHelper = areasHelper;
    }

    @Override
    public void updateLocation(LatLng location) {
        groupAreas = areasHelper.getGroupAreas(location.latitude, location.longitude);
        firebase.updateMyLocation(location, groupAreas.getMainArea().getId(), new FirebaseActionListenerCallback() {
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

    @Override
    public void removeDriverFromArea() {
        if(groupAreas!=null) {
            firebase.removeDriverFromArea(groupAreas, new FirebaseActionListenerCallback() {
                @Override
                public void onSuccess() {
                    Log.d("d", "HomeRepositoryImpl().removeDriverFromArea on Sucess()");
                }
                @Override
                public void onError(DatabaseError error) {
                    post(error.getMessage());
                }
            });
            groupAreas = null;
        }

    }

    @Override
    public void uploadNearDriverData(NearDriver nearDriver) {
        groupAreas = areasHelper.getGroupAreas(nearDriver.getLatitude(), nearDriver.getLongitude());
        firebase.uploadNearDriverData(nearDriver, groupAreas.getMainArea().getId(), new FirebaseActionListenerCallback(){

            @Override
            public void onSuccess() {
                Log.d("d", "HomeRepositoryImpl().uploadNearDriverData on Sucess()");
            }
            @Override
            public void onError(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void acceptTravel(Travel travel) {
        groupAreas = areasHelper.getGroupAreas(travel.getLatDriver(), travel.getLongDriver());
        firebase.acceptTravel(travel, groupAreas.getMainArea().getId());
    }

    private void post(String error){
        MapHomeEvent event = new MapHomeEvent();
        event.setError(error);
        eventBus.post(event);
    }

}
