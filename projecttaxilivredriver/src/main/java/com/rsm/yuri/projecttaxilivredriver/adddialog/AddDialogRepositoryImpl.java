package com.rsm.yuri.projecttaxilivredriver.adddialog;

import com.google.firebase.database.DatabaseError;
import com.rsm.yuri.projecttaxilivredriver.adddialog.AddDialogEvent.AddDialogEvent;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

/**
 * Created by yuri_ on 10/11/2017.
 */

public class AddDialogRepositoryImpl implements AddDialogRepository {

    FirebaseAPI firebase;
    EventBus eventBus;

    public AddDialogRepositoryImpl(FirebaseAPI firebase, EventBus eventBus) {
        this.firebase = firebase;
        this.eventBus = eventBus;
    }

    @Override
    public void add(String key, final String value){
        key = key.replace(".","_");

        final String finalKey = key;
        firebase.updateKeyValueUser(key, value, new FirebaseActionListenerCallback(){
            @Override
            public void onSuccess() {
                post(finalKey, value);
            }

            @Override
            public void onError(DatabaseError error) {
                post(error.getMessage());
            }
        });

    }

    private void post(String key, String value) {
        post(key, value, null);
    }

    private void post(String errorMsg){
        post(null, null, errorMsg);
    }

    private void post(String key, String value, String errorMsg){
        AddDialogEvent event = new AddDialogEvent();
        event.setKey(key);
        event.setValue(value);
        event.setErrorMsg(errorMsg);
        eventBus.post(event);
    }

}
