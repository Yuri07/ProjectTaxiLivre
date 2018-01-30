package com.rsm.yuri.projecttaxilivre.domain;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class FirebaseAPI {

    private  final static String DRIVER_PATH = "drivers";
    private  final static String CAR_PATH = "cars";
    private final static String USERS_PATH = "users";
    private final static String CHATS_PATH = "chats";
    public final static String HISTORICCHATS_PATH = "historicchats";
    private  final static String LOCATION_PATH = "location";
    private  final static String RATINGS_PATH = "ratings";
    private final static String SEPARATOR = "___";


    private DatabaseReference dataReference;


    public FirebaseAPI(DatabaseReference databaseReference){
        dataReference = databaseReference;//FirebaseDatabase.getInstance().getReference();
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
        dataReference.addValueEventListener(postListener);
    }

    /*public String getAuthEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getEmail();
        }
        return null;
    }*/

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
        //notifyContactsOfConnectionChange(User.OFFLINE, true);
        changeUserConnectionStatus(User.OFFLINE);
    }

    public DatabaseReference getMyUserReference(){
        return getUserReference(getAuthUserEmail());
    }

    public String getAuthUserEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        if(user!=null)
            email = user.getEmail();

        return email;
    }

    public DatabaseReference getUserReference(String email){
        DatabaseReference userReference = null;
        if(email!=null){
            String emailKey = email.replace(".","_");
            userReference = dataReference.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    public DatabaseReference getContactsReference(String email){
        return getUserReference(email).child(HISTORICCHATS_PATH);
    }

    public DatabaseReference getMyContactsReference(){
        return getContactsReference(getAuthUserEmail());
    }

    public DatabaseReference getOneContactReference(String mainEmail, String childEmail){
        String childKey = childEmail.replace(".","_");
        return getUserReference(mainEmail).child(HISTORICCHATS_PATH).child(childKey);
    }

    public DatabaseReference getChatsReference(String receiver){
        String keySender = getAuthUserEmail().replace(".","_");
        String keyReceiver = receiver.replace(".","_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0) {//Esse método retorna um numero inteiro. Se ele for menor do que zero, o primeiro argumento é "menor" (alfabeticamente, nesse caso) que o segundo; maior que zero se o primeiro for "maior" que o segundo, e igual a zero se eles forem iguais. Esse método diferencia maiúsculas de minúsuclas. Se não quiser isso, use o compareToIgnoreCase
            keyChat = keyReceiver + SEPARATOR + keySender;//sempre o primeiro em ordem alfabetica vem primeiro
        }
        return dataReference.getRoot().child(CHATS_PATH).child(keyChat);
    }

    public void changeUserConnectionStatus(int status) {
        if (getMyUserReference() != null) {
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("status", status);
            getMyUserReference().updateChildren(updates);

            notifyContactsOfConnectionChange(status);
        }
    }

    public void notifyContactsOfConnectionChange(final int status, final boolean signoff) {
        final String myEmail = getAuthUserEmail();
        getMyContactsReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String email = child.getKey();//um email de um contato meu
                    DatabaseReference reference = getOneContactReference(email, myEmail);//pegando a
                    // referencia do meu contato para  avisa-lo que estou online ou offline(tornar true
                    // ou false o valor de 'users/email/contacts/myEmail').
                    reference.setValue(status);
                }
                if (signoff){
                    FirebaseAuth.getInstance().signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public void notifyContactsOfConnectionChange(int status) {
        notifyContactsOfConnectionChange(status, false);
    }

    /*public void signOff(){
        notifyContactsOfConnectionChange(User.OFFLINE, true);
    }*/

}
