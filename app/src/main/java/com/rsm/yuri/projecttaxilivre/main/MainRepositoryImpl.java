package com.rsm.yuri.projecttaxilivre.main;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.main.events.MainEvent;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MainRepositoryImpl implements MainRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;

    private DatabaseReference myUserReference;

    public MainRepositoryImpl(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;

        myUserReference = firebase.getMyUserReference();
    }

    @Override
    public void logout() {
        firebase.logout();
    }

    @Override
    public void checkForSession() {
        firebase.checkForSession(new FirebaseActionListenerCallback(){
            @Override
            public void onSuccess() {

                myUserReference = firebase.getMyUserReference();//pega a referencia no
                // database para o usuario atualmente autenticado
                myUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        initSignIn(snapshot);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        post(MainEvent.onFailedToRecoverSession, firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                post(MainEvent.onFailedToRecoverSession);
            }
        });
    }

    private void initSignIn(DataSnapshot snapshot){
        User currentUser = snapshot.getValue(User.class);

        registerNewUser();

        firebase.changeUserConnectionStatus(User.ONLINE);
        String email = firebase.getAuthUserEmail();
        post(MainEvent.onSuccessToRecoverSession, null, email);
    }

    private void registerNewUser() {
        String email = firebase.getAuthUserEmail();
        if (email != null) {
            User currentUser = new User(email, true, null);
            myUserReference.setValue(currentUser);
        }
    }

    @Override
    public void changeUserConnectionStatus(boolean online) {
        firebase.changeUserConnectionStatus(online);
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String errorMessage) {
        post(type, errorMessage, null);
    }

    private void post(int type, String errorMessage, String loggedUserEmail) {
        MainEvent mainEvent = new MainEvent();
        mainEvent.setEventType(type);
        if (errorMessage != null) {
            mainEvent.setErrorMessage(errorMessage);
        }
        mainEvent.setLoggedUserEmail(loggedUserEmail);
        eventBus.post(mainEvent);
    }

}
