package com.rsm.yuri.projecttaxilivre.historicchatslist;

import android.text.TextUtils;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivre.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListRepositoryImpl implements HistoricChatsListRepository {

    private FirebaseAPI firebase;
    private EventBus eventBus;

    public HistoricChatsListRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void subscribeForHistoricChatListUpdates() {
        /*firebase.checkForData(new FirebaseActionListenerCallback() {//addvalueEventListener
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(DatabaseError error) {
                if (error != null) {
                    post(HistoricChatsListEvent.ERROR_EVENT, error.getMessage());
                } else {//error==null não ha postagens(via FirebaseAPI.checkForData())
                    post(HistoricChatsListEvent.ERROR_EVENT, "");
                }

            }
        });*/
        firebase.subscribeForHistoricChatsListUpdates(new FirebaseEventListenerCallback() {//addChildEventListener
            @Override public void onChildAdded(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();
                Driver driver = new Driver();
                driver.setEmail(email);
                driver.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatAdded, driver);
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot){
                String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();
                Driver driver = new Driver();
                driver.setEmail(email);
                driver.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatChanged, driver);
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot){

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();
                Driver driver = new Driver();
                driver.setEmail(email);
                driver.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatRemoved, driver);
            }

            @Override public void onCancelled(DatabaseError error) {
                post(HistoricChatsListEvent.ERROR_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void unSubscribeForHistoricChatListUpdates() {
        firebase.unSubscribeForHistoricChatsListUpdates();
    }

    @Override
    public void changeUserConnectionStatus(int status) {
        firebase.changeUserConnectionStatus(status);
    }

    /*@Override//essa funcao esta implementada na MainActivity
    public void changeUserConnectionStatus(int status) {
        firebase.changeUserConnectionStatus(status);
    }*/

    @Override
    public void removeHistoricChat(String email) {
        firebase.removeHistoricChat(email);
    }

    @Override
    public void destroyHistoricChatListListener() {
        firebase.destroyHistoricChatsListener();

    }

    private void post(int type, Driver driver){
        post(type, driver, null);
    }

    private void post(int type, String error){
        post(type, null, error);
    }

    private void post(int type, Driver driver, String error){
        HistoricChatsListEvent event = new HistoricChatsListEvent();
        event.setEventType(type);
        event.setError(error);
        event.setDriver(driver);
        eventBus.post(event);
    }

}
