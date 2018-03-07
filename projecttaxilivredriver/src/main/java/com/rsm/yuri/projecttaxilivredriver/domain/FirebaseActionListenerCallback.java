package com.rsm.yuri.projecttaxilivredriver.domain;

import com.google.firebase.database.DatabaseError;

/**
 * Created by yuri_ on 31/01/2018.
 */

public interface FirebaseActionListenerCallback {

    void onSuccess();
    void onError(DatabaseError error);

}
