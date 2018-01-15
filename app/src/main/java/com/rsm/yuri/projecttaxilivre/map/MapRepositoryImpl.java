package com.rsm.yuri.projecttaxilivre.map;

import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MapRepositoryImpl implements MapRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;

    public MapRepositoryImpl(EventBus eventBus, FirebaseAPI firebase) {
        this.eventBus = eventBus;
        this.firebase = firebase;
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
                String email = firebase.getAuthEmail();
                post(MapEvent.onSuccessToRecoverSession, null, email);
            }

            @Override
            public void onError(DatabaseError error) {
                post(MapEvent.onFailedToRecoverSession);
            }
        });
    }

    private void post(int type) {
        post(type, null);
    }

    private void post(int type, String errorMessage) {
        post(type, errorMessage, null);
    }

    private void post(int type, String errorMessage, String loggedUserEmail) {
        MapEvent mapEvent = new MapEvent();
        mapEvent.setEventType(type);
        if (errorMessage != null) {
            mapEvent.setErrorMessage(errorMessage);
        }
        mapEvent.setLoggedUserEmail(loggedUserEmail);
        eventBus.post(mapEvent);
    }

}
