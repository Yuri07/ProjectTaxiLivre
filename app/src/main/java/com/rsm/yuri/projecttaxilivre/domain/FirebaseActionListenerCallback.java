package com.rsm.yuri.projecttaxilivre.domain;

import com.google.firebase.database.DatabaseError;

/**
 * Created by yuri_ on 12/01/2018.
 */

public interface FirebaseActionListenerCallback {

    void onSuccess();
    void onError(DatabaseError error);

}
