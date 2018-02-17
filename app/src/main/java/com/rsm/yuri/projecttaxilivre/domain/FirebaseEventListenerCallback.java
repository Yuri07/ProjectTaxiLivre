package com.rsm.yuri.projecttaxilivre.domain;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by yuri_ on 12/01/2018.
 */

public interface FirebaseEventListenerCallback {

    void onChildAdded(DataSnapshot dataSnapshot);
    void onChildChanged(DataSnapshot dataSnapshot);
    void onChildRemoved(DataSnapshot dataSnapshot);
    void onChildMoved(DataSnapshot dataSnapshot);
    void onCancelled(DatabaseError error);

}
