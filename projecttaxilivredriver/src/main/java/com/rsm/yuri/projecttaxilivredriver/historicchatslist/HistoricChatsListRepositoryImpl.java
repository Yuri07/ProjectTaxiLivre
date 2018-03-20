package com.rsm.yuri.projecttaxilivredriver.historicchatslist;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListRepositoryImpl implements HistoricChatsListRepository {

    FirebaseAPI firebase;
    EventBus eventBus;

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
                User user = new User();
                user.setEmail(email);
                user.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatAdded, user);
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot){
                String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();
                User user = new User();
                user.setEmail(email);
                user.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatChanged, user);
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot){

            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                String email = dataSnapshot.getKey();
                email = email.replace("_",".");
                long online = ((Long)dataSnapshot.getValue()).longValue();
                User user = new User();
                user.setEmail(email);
                user.setStatus(online);

                post(HistoricChatsListEvent.onHistoricChatRemoved, user);
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

    @Override
    public void getUrlPhotoUser(final User user) {
        firebase.getUrlPhotoDriver(user.getEmail(), new FirebaseEventListenerCallback(){

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                String url = (String) dataSnapshot.getValue();
                User userUrlUpdated = user;
                userUrlUpdated.setUrlPhotoUser(url);
                post(HistoricChatsListEvent.onUrlPhotoDriverRetrived, userUrlUpdated);
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
                post(HistoricChatsListEvent.ERROR_EVENT, error.getMessage());
            }
        });
    }

    @Override
    public void removeHistoricChat(String email) {
        firebase.removeHistoricChat(email);
    }

    @Override
    public void destroyHistoricChatListListener() {
        firebase.destroyHistoricChatsListener();

    }

    private void post(int type, User user){
        post(type, user, null);
    }

    private void post(int type, String error){
        post(type, null, error);
    }

    private void post(int type, User user, String error){
        HistoricChatsListEvent event = new HistoricChatsListEvent();
        event.setEventType(type);
        event.setError(error);
        event.setUser(user);
        eventBus.post(event);
    }

}
