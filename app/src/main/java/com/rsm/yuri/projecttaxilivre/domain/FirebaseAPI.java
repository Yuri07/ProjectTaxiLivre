package com.rsm.yuri.projecttaxilivre.domain;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class FirebaseAPI {

    private  final static String DRIVER_PATH = "drivers";
    private  final static String CAR_PATH = "cars";
    private final static String USERS_PATH = "users";
    private final static String CHATS_PATH = "chats";
    public final static String HISTORICCHATS_PATH = "historicchats";
    private final static String SEPARATOR = "___";


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

    public String getAuthEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }

    public void checkForSession(FirebaseActionListenerCallback listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            listener.onSuccess();
        } else {
            listener.onError(null);
        }
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

}
