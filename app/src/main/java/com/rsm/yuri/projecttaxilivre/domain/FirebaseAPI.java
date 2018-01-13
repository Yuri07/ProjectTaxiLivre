package com.rsm.yuri.projecttaxilivre.domain;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class FirebaseAPI {

    private DatabaseReference mPhotoDatabaseReference;//private Firebase firebase; Firebase trocado por DatabaseReference
    private ChildEventListener photosEventListener;

    public FirebaseAPI(DatabaseReference databaseReference){
        mPhotoDatabaseReference = databaseReference;//FirebaseDatabase.getInstance().getReference();
    }

    public void checkForData(final FirebaseActionListenerCallback listener){
        ValueEventListener postListener = new ValueEventListener() {
            @Override public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    listener.onSuccess();
                } else {
                    listener.onError(null);
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                Log.d("FIREBASE API", databaseError.getMessage());
            }
        };
        mPhotoDatabaseReference.addValueEventListener(postListener);
    }

}
