package com.rsm.yuri.projecttaxilivredriver.home;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.home.events.MapHomeEvent;
import com.rsm.yuri.projecttaxilivredriver.home.models.AreasFortalezaHelper;
import com.rsm.yuri.projecttaxilivredriver.home.models.AreasHelper;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
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
    private String cidade;

    private DatabaseReference myUserReference;

    public HomeRepositoryImpl(FirebaseAPI firebase, EventBus eventBus/*, GroupAreas groupAreas*/,
                              AreasHelper areasHelper, String cidade) {
        this.firebase = firebase;
        this.eventBus = eventBus;
        this.groupAreas = null;
        //this.groupAreas = groupAreas;
        this.areasHelper = areasHelper;
        this.cidade = cidade;
    }

    @Override
    public void updateLocation(LatLng location) {
        groupAreas = areasHelper.getGroupAreas(location.latitude, location.longitude);
        /*firebase.updateMyLocation(location, cidade, groupAreas.getMainArea().getId(), new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                Log.d("d", "HomeRepositoryImpl().updateLocation on Sucess()");
            }
            @Override
            public void onError(DatabaseError error) {
                post(error.getMessage());
            }
        });*/
    }

    @Override
    public void removeDriverFromArea() {
        if(groupAreas!=null) {
            firebase.removeDriverFromArea(cidade, groupAreas, new FirebaseActionListenerCallback() {
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
        firebase.uploadNearDriverData(nearDriver, cidade, groupAreas.getMainArea().getId(), new FirebaseActionListenerCallback(){

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
        Log.d("d", "HomeRepositoryImpl.acceptTravel()");
        groupAreas = areasHelper.getGroupAreas(travel.getLatDriver(), travel.getLongDriver());
        String newTravelID = firebase.acceptTravel(travel, cidade, groupAreas.getMainArea().getId());
        Log.d("d", "HomeRepositoryImpl.acceptTravel() - newTravelId: " + newTravelID);
        post(MapHomeEvent.ON_TRAVEL_CREATED, newTravelID);
    }

    @Override
    public void notifyRequesterTravelNotAccepted(String requesterEmail) {
        firebase.notifyRequesterTravelNotAccepted(requesterEmail);
    }

    @Override
    public void updateLocationForTravel(LatLng location, String requesterEmail, String travelID) {
        firebase.updateMyLocationForTravel(location, requesterEmail, travelID, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                Log.d("d", "HomeRepositoryImpl().updateLocationForTravel on Sucess()");

            }
            @Override
            public void onError(DatabaseError error) {
                post(error.getMessage());
            }
        });
    }

    @Override
    public void initiateTravel(String emailRequester, String travelID) {
        firebase.initiateTravel(emailRequester, travelID);
    }

    @Override
    public void terminateTravel(String emailRequester, String travelID) {
        firebase.terminateTravel(emailRequester, travelID);
    }

    @Override
    public void uploadUserRating(String userEmail, Rating rating) {
        firebase.uploadUserRating(userEmail, rating);
    }

    @Override
    public void saveCity(String cidade) {
        firebase.saveCity(cidade);
    }

    @Override
    public void retrieveDataUser() {
        firebase.checkForSession(new FirebaseActionListenerCallback(){
            @Override
            public void onSuccess() {

                myUserReference = firebase.getMyUserReference();//pega a referencia no
                // database para o usuario atualmente autenticado

                //myUserReference.keepSynced(true);

                myUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        postOnSuccessRetrieveDataUser(snapshot);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        post(firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                post("Falha em recuperar a sessao");
            }
        });
    }

    private void postOnSuccessRetrieveDataUser(DataSnapshot snapshot){
        Driver currentUser = snapshot.getValue(Driver.class);//null caso seja a primeira vez que o metodo e executado para esse usuario
        post(MapHomeEvent.onSuccessToGetDateUser, currentUser);
    }

    private void post(int type, String newTravelID){
        Log.d("d", "HomeRepositoryImpl.post(type, newTravelID)");
        post( type, newTravelID, null, null );
    }

    private void post(int type, Driver currentUser){
        post(type,null,currentUser,null);
    }

    private void post(String error){
        post(-1, null, null, error);
    }

    private void post(int type, String newTravelID, Driver user,String error){
        MapHomeEvent event = new MapHomeEvent();
        event.setNewTravelID(newTravelID);
        event.setEventType(type);
        event.setError(error);
        Log.d("d", "HomeRepositoryImpl.post(type, newTravelID, error)");
        eventBus.post(event);
    }

}
