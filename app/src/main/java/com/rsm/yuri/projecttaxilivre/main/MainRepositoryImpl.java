package com.rsm.yuri.projecttaxilivre.main;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rsm.yuri.projecttaxilivre.R;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.main.events.MainEvent;
import com.rsm.yuri.projecttaxilivre.main.ui.MainActivity;

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

                //myUserReference.keepSynced(true);

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
                post(MainEvent.onFailedToRecoverSession,"Falha em recuperar a sessao");
            }
        });
    }

    private void initSignIn(DataSnapshot snapshot){
        User currentUser = snapshot.getValue(User.class);//null caso seja a primeira vez que o metodo e executado para esse usuario
        if (currentUser == null) {
            currentUser = new User(firebase.getAuthUserEmail(), 1, null);
            registerNewUser(currentUser);
        }

        firebase.changeUserConnectionStatus(User.ONLINE);
        post(MainEvent.onSuccessToRecoverSession, null, currentUser);
    }

    private void registerNewUser(User newUser) {
        if (newUser.getEmail()!= null) {
            myUserReference.setValue(newUser);
        }
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

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String errorMessage) {
        post(type, errorMessage, null);
    }

    private void post(int type, String errorMessage, User loggedUser){//String loggedUserEmail) {
        MainEvent mainEvent = new MainEvent();
        mainEvent.setEventType(type);
        if (errorMessage != null) {
            mainEvent.setErrorMessage(errorMessage);
        }
        mainEvent.setLoggedUser(loggedUser);
        eventBus.post(mainEvent);
    }

}
