package com.rsm.yuri.projecttaxilivredriver.domain;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.main.models.AreasHelper;
import com.rsm.yuri.projecttaxilivredriver.main.models.GroupAreas;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class FirebaseAPI {

    private final static String DRIVER_PATH = "drivers";
    private final static String CAR_PATH = "cars";
    private final static String USERS_PATH = "users";
    private final static String NOME_PATH = "nome";
    private final static String URL_PHOTO_USER_PATH = "urlPhotoUser";
    private final static String URL_PHOTO_DRIVER_PATH = "urlPhotoDriver";
    private final static String NEAR_DRIVERS_PATH = "neardrivers";
    private final static String AREAS_PATH = "areas";
    private final static String CHATS_PATH = "chats";
    private final static String HISTORICCHATS_PATH = "historicchats";
    private final static String LOCATION_PATH = "location";
    private final static String RATINGS_PATH = "ratings";
    private final static String LATITUDE_PATH = "latitude";
    private final static String LONGITUDE_PATH = "longitude";
    private final static String SEPARATOR = "___";


    private static final String DRIVERS_PHOTOS_PATH = "drivers_photos";
    private static final String USERS_PHOTOS_PATH = "users_photos";

    private AreasHelper areasHelper;

    private DatabaseReference databaseReference;

    private ChildEventListener historicChatsListEventListener;
    private ChildEventListener chatEventListener;

    private ValueEventListener ratingsEventListener;
    private ValueEventListener carEventListener;

    private StorageReference storageReference;
    private StorageReference driversPhotosStorageReference;
    private StorageReference usersPhotosStorageReference;

    public FirebaseAPI(DatabaseReference databaseReference, StorageReference storageReference, AreasHelper areasHelper){
        this.databaseReference = databaseReference;
        this.storageReference = storageReference;
        this.areasHelper = areasHelper;
    }

    public void checkForData(final FirebaseActionListenerCallback listener){//checa se tem dados(era usado no PhotoFeed App para verificar se a lista de fotos estava vazia).
        ValueEventListener postListener = new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    listener.onSuccess();
                } else {
                    listener.onError(null);
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                //Log.d("FIREBASE API", databaseError.getMessage());
                listener.onError(databaseError);
            }
        };
        databaseReference.addValueEventListener(postListener);
    }


    public void subscribeForHistoricChatsListUpdates(final FirebaseEventListenerCallback listener) {
        if(historicChatsListEventListener==null) {
            historicChatsListEventListener= new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    listener.onChildChanged(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    listener.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };

            getMyHistoricChatsReference().addChildEventListener(historicChatsListEventListener);

        }
    }

    public void unSubscribeForHistoricChatsListUpdates() {
        getMyHistoricChatsReference().removeEventListener(historicChatsListEventListener);
    }

    public void subscribeForChatUpdates(final String receiver, final FirebaseEventListenerCallback listener) {
        if(chatEventListener==null) {
            chatEventListener= new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };

            getChatsReference(receiver).addChildEventListener(chatEventListener);

        }
    }

    public void unSubscribeForChatUpdates(String receiver) {
        getChatsReference(receiver).removeEventListener(chatEventListener);
    }

    public void updateMyLocation(final LatLng location, final FirebaseActionListenerCallback listenerCallback){
        String authUserEmail = getAuthUserEmail();
        GroupAreas groupAreas = areasHelper.getGroupAreas(location.latitude, location.longitude);
        NearDriver nearDriver = new NearDriver(getAuthUserEmail(),location.latitude, location.longitude );
        getAreaDataReference(groupAreas.getMainArea().getId()).child(authUserEmail.replace(".","_"))
                .setValue(nearDriver, new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            getMyUserReference().child(LATITUDE_PATH).setValue(location.latitude);
                            getMyUserReference().child(LONGITUDE_PATH).setValue(location.longitude);
                            listenerCallback.onSuccess();
                        }else{
                            listenerCallback.onError(databaseError);
                        }
                    }
                });

    }

    public void updateKeyValueUser(final String key, final String value, final FirebaseActionListenerCallback listenerCallback) {

        getMyUserReference().child(key).setValue(value, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if(error==null){
                    listenerCallback.onSuccess();
                }else{
                    listenerCallback.onError(error);
                }
            }
        });
    }

    public void updateProfile(Driver driver, Car car, final FirebaseActionListenerCallback listener) {

        Map<String, Object> carValues = car.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/"+DRIVER_PATH+"/"+ driver.getEmail().replace(".","_") +"/"+NOME_PATH, driver.getNome());
        childUpdates.put("/"+CAR_PATH+"/" + car.getEmail().replace(".","_") , carValues);

        databaseReference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference databaseReference) {
                if(error==null){
                    listener.onSuccess();
                }else{
                    listener.onError(error);
                }
            }
        });

    }

    public void getMyCar(final FirebaseEventListenerCallback listenerCallback) {
        carEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listenerCallback.onChildAdded(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onCancelled(databaseError);
            }
        };
        getMyCarReference().addListenerForSingleValueEvent(carEventListener);

    }

    public void retrieveRatings(final String email, final FirebaseEventListenerCallback listenerCallback){
        ratingsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("d", "FirebaseAPI.retrieveRatings().onDataChange(): "+rating.getEmail());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    listenerCallback.onChildAdded(postSnapshot);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onCancelled(databaseError);
            }
        };
        getMyRatingsReference().addListenerForSingleValueEvent(ratingsEventListener);
    }

    /*public void getLoggedUser(final FirebaseActionListenerCallback listener){
        getMyUserReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                initSignIn(snapshot, listener);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                listener.onError(firebaseError);
            }
        });
    }

    private void initSignIn(DataSnapshot snapshot, FirebaseActionListenerCallback listener){
        User currentUser = snapshot.getValue(User.class);//null caso seja a primeira vez que o metodo e executado para esse usuario
        if (currentUser == null) {
            currentUser = new User(getAuthUserEmail(), 1, null);
            registerNewUser(currentUser);
        }

        changeUserConnectionStatus(User.ONLINE);
        listener
        post(MainEvent.onSuccessToRecoverSession, null, currentUser);
    }

    private void registerNewUser(User newUser) {
        if (newUser.getEmail()!= null) {
            myUserReference.setValue(newUser);
        }
    }*/

    public DatabaseReference getMyCarReference(){
        DatabaseReference carReference = null;
        String email = getAuthUserEmail();
        if(email!=null){
            String emailKey = email.replace(".","_");
             carReference = databaseReference.getRoot().child(CAR_PATH).child(emailKey);
        }
        //Log.d("d", "FaribaseAPI.getMyRatingReference(): "+ratingsReference);
        return carReference;
    }

    public DatabaseReference getMyRatingsReference(){
        //return getRatingsReference(getAuthUserEmail());
        DatabaseReference ratingsReference = null;
        String email = getAuthUserEmail();
        if(email!=null){
            String emailKey = email.replace(".","_");
            ratingsReference = databaseReference.getRoot().child(RATINGS_PATH).child(emailKey);
        }
        //Log.d("d", "FaribaseAPI.getMyRatingReference(): "+ratingsReference);
        return ratingsReference;
    }

    /*public DatabaseReference getRatingsReference(String email){
        //return getDriverReference(email).child(RATINGS_PATH).child();

    }*/

    public DatabaseReference getMyHistoricChatsReference(){
        return getHistoriChatsReferenceOfDriver(getAuthUserEmail());
    }

    public DatabaseReference getHistoriChatsReferenceOfDriver(String email){
        return getDriverReference(email).child(HISTORICCHATS_PATH);
    }

    public DatabaseReference getHistoriChatsReferenceOfUser(String email){
        return getUserReference(email).child(HISTORICCHATS_PATH);
    }

    private DatabaseReference getAreaDataReference(String idArea) {
        DatabaseReference areaReference = null;
        if(idArea!=null){
            areaReference = databaseReference.getRoot().child(AREAS_PATH).child(idArea);
        }
        return areaReference;

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
        //notifyContactsOfConnectionChange(User.OFFLINE, true);
        changeUserConnectionStatus(Driver.OFFLINE);
    }

    public DatabaseReference getMyUserReference(){
        return getDriverReference(getAuthUserEmail());
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
            userReference = databaseReference.getRoot().child(USERS_PATH).child(emailKey);
        }
        return userReference;
    }

    public DatabaseReference getDriverReference(String email){
        DatabaseReference driverReference = null;
        if(email!=null){
            String emailKey = email.replace(".","_");
            driverReference = databaseReference.getRoot().child(DRIVER_PATH).child(emailKey);
        }
        return driverReference;
    }

    /*public DatabaseReference getContactsReference(String email){
        return getUserReference(email).child(HISTORICCHATS_PATH);
    }*/

    /*public DatabaseReference getMyContactsReference(){
        return getContactsReference(getAuthUserEmail());
    }*/

    public DatabaseReference getOneContactReferenceofUser(String mainEmail, String childEmail){
        String childKey = childEmail.replace(".","_");
        return getUserReference(mainEmail).child(HISTORICCHATS_PATH).child(childKey);
    }

    public DatabaseReference getOneContactReferenceofDriver(String mainEmail, String childEmail){
        String childKey = childEmail.replace(".","_");
        return getDriverReference(mainEmail).child(HISTORICCHATS_PATH).child(childKey);
    }

    public DatabaseReference getChatsReference(String receiver){
        String keySender = getAuthUserEmail().replace(".","_");
        String keyReceiver = receiver.replace(".","_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0) {//Esse método retorna um numero inteiro. Se ele for menor do que zero, o primeiro argumento é "menor" (alfabeticamente, nesse caso) que o segundo; maior que zero se o primeiro for "maior" que o segundo, e igual a zero se eles forem iguais. Esse método diferencia maiúsculas de minúsuclas. Se não quiser isso, use o compareToIgnoreCase
            keyChat = keyReceiver + SEPARATOR + keySender;//sempre o primeiro em ordem alfabetica vem primeiro
        }
        return databaseReference.getRoot().child(CHATS_PATH).child(keyChat);
    }

    public void changeUserConnectionStatus(long status) {
        if (getMyUserReference() != null) {
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("status", status);
            getMyUserReference().updateChildren(updates);

            notifyContactsOfConnectionChange(status);
        }
    }

    public void notifyContactsOfConnectionChange(final long status) {
        final String myEmail = getAuthUserEmail();
        getMyHistoricChatsReference().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String email = child.getKey();//um email de um contato meu
                    //Log.d("d", "firebase.HistoricChat.onDataChange "+email);
                    DatabaseReference reference = getOneContactReferenceofUser(email, myEmail);//pegando a
                    // referencia do meu contato para  avisa-lo que estou online ou offline(tornar true
                    // ou false o valor de 'users/email/contacts/myEmail').
                    reference.setValue(status);
                }

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
    }

    public DatabaseReference getMyUrlPhotoReference() {
        return getMyUserReference().child(URL_PHOTO_DRIVER_PATH);
    }

    public void updateAvatarPhoto(final Uri selectedImageUri, final FirebaseStorageFinishedListener firebaseStorageFinishedListener) {//,(String previousImageUrl,

        StorageReference photoRef = getDriversPhotosStorageReference().child(getAuthUserEmail().replace(".","_"));

        final DatabaseReference myUrlPhotoReference = getMyUrlPhotoReference();

        photoRef.putFile(selectedImageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                myUrlPhotoReference.setValue(downloadUrl.toString());
                firebaseStorageFinishedListener.onSuccess(downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseStorageFinishedListener.onError(e.getMessage());
            }
        });

    }

    public StorageReference getDriversPhotosStorageReference(){
        return storageReference.getRoot().child(DRIVERS_PHOTOS_PATH);
    }

    public StorageReference getUsersPhotosStorageReference(){
        return storageReference.getRoot().child(USERS_PHOTOS_PATH);
    }

    public void removeHistoricChat(String email) {
        Log.d("d", "firebase.removeHistoricChat");
        String currentUserEmail = getAuthUserEmail();
        getOneContactReferenceofUser(currentUserEmail, email).removeValue();
        getOneContactReferenceofDriver(email, currentUserEmail).removeValue();//fazer alteracao para não remover historico do chat para no app do motorista

    }

    public void destroyHistoricChatsListener(){
        historicChatsListEventListener = null;
    }

    public void destroyChatListener() {
        chatEventListener = null;
    }

}
