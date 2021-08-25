package com.rsm.yuri.projecttaxilivredriver.main;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.editprofile.events.EditProfileEvent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.main.events.MainEvent;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MainRepositoryImpl implements MainRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;

    private DatabaseReference myUserReference;
    //private Driver loggedDriver;

    public MainRepositoryImpl(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;

        myUserReference = firebase.getMyUserReference();
        //loggedDriver = new Driver();
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
                        //Log.d("d", "Nao conseguio ler na referencia para meu usuario: " + firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                //Log.d("d", "Nao existe sessao aberta no servidor: metodo post");
                post(MainEvent.onFailedToRecoverSession, "Falha em recuperar sessao");
            }
        });
    }

    private void initSignIn(DataSnapshot snapshot){
        Driver currentUser = snapshot.getValue(Driver.class);//null caso seja a primeira vez que o metodo e executado para esse usuario
        if (currentUser == null) {
            currentUser = new Driver(firebase.getAuthUserEmail(), 1, null);
            registerNewUser(currentUser);
        }
        firebase.changeUserConnectionStatus(Driver.ONLINE);
        post(MainEvent.onSuccessToRecoverSession, null, currentUser, null);

    }

    private void registerNewUser(Driver newUser) {
        if (newUser.getEmail()!= null) {
            myUserReference.setValue(newUser);
        }
    }

    @Override
    public void getMyCar() {
        firebase.getMyCar(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Car car = dataSnapshot.getValue(Car.class);
                post(MainEvent.onSuccessToRecoverMyCar,null, null, car);
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
                post(MainEvent.onFailedToRecoverMyCar, error.getMessage());
            }
        });
    }

    @Override
    public void changeUserConnectionStatus(int status) {
        firebase.changeUserConnectionStatus(status);
    }

    @Override
    public void verifyToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("d", "Fetching FCM registration token failed",
                                    task.getException());
                            post(MainEvent.onFailedToSaveFirebaseTokenInServer,
                                    "getToken() Task dont complete!");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        sendFirebaseNotificationTokenToServer(token);

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("d", "FirebaseMessaging.getToken()");
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //@Override
    public void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken) {
        firebase.sendTokenToServer(firebaseNotificationToken, new FirebaseActionListenerCallback() {
            @Override
            public void onSuccess() {
                post(MainEvent.onSucceessToSaveFirebaseTokenInServer);
            }

            @Override
            public void onError(DatabaseError error) {
                post(MainEvent.onFailedToSaveFirebaseTokenInServer, "Usuario nulo");
            }
        });

    }

    private void post(int type){
        post(type, null, null, null);
    }

    private void post(int type, String errorMessage) {
        post(type, errorMessage, null, null);
    }

    private void post(int type, Driver loggedUser){
        post(type, null, loggedUser, null);
    }

    private void post(int type, Car myCar) {
        post(type, null, null, myCar);
    }

    private void post(int type, String errorMessage, Driver loggedUser, Car myCar){
        MainEvent mainEvent = new MainEvent();
        mainEvent.setEventType(type);
        if (errorMessage != null) {
            mainEvent.setErrorMessage(errorMessage);
        }
        mainEvent.setLoggedUser(loggedUser);
        mainEvent.setMyCar(myCar);
        eventBus.post(mainEvent);
    }

}
