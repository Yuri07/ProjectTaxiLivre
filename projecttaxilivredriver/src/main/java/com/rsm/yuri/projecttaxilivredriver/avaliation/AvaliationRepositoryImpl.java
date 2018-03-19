package com.rsm.yuri.projecttaxilivredriver.avaliation;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.avaliation.events.AvaliationEvent;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

/**
 * Created by yuri_ on 07/03/2018.
 */

public class AvaliationRepositoryImpl implements AvaliationRepository {

    private FirebaseAPI firebase;
    private EventBus eventBus;

    public AvaliationRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void retrieveRatings(String email) {
        firebase.retrieveRatings(email, new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Rating rating = dataSnapshot.getValue(Rating.class);
                //Log.d("d", "AvaliationRepository.retrieveRatings(): "+rating.getEmail());
                post(AvaliationEvent.READ_EVENT, rating);
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
                post(AvaliationEvent.ERROR_EVENT, error.getMessage());
            }
        });
    }

    private void post(int type, Rating rating){
        post(type, rating, null);
    }

    private void post(int type, String error){
        post(type, null, error);
    }

    private void post(int type, Rating rating, String error){
        AvaliationEvent avaliationEvent = new AvaliationEvent();
        avaliationEvent.setRating(rating);
        avaliationEvent.setEventType(type);
        avaliationEvent.setError(error);
        eventBus.post(avaliationEvent);
    }

}
