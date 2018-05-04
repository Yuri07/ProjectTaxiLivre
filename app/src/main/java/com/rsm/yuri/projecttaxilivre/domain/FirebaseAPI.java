package com.rsm.yuri.projecttaxilivre.domain;

import android.net.Uri;
import android.provider.ContactsContract;
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
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;
import com.rsm.yuri.projecttaxilivre.map.models.AreasHelper;
import com.rsm.yuri.projecttaxilivre.map.models.GroupAreas;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class FirebaseAPI {

    private final static String DRIVER_PATH = "drivers";
    private final static String CAR_PATH = "cars";
    private final static String USERS_PATH = "users";
    private final static String STATUS_PATH = "status";
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

    final static String NOTIFICATION_TOKEN_PATH = "notificationToken";
    final static String TOKEN_PATH = "token";

    private static final String DRIVERS_PHOTOS_PATH = "drivers_photos";
    private static final String USERS_PHOTOS_PATH = "users_photos";

    //private AreasHelper areasHelper;

    private DatabaseReference databaseReference;

    private DatabaseReference mainAreaDataReference;
    private DatabaseReference areaVerticalSideDataReference;
    private DatabaseReference areaHorizontalSideDataReference;
    private DatabaseReference areaDiagonalDataReference;

    private ChildEventListener areasEventListener;
    private ChildEventListener historicChatsListEventListener;
    private ChildEventListener chatEventListener;

    private ValueEventListener statusReceiverChatEventListener;

    private StorageReference storageReference;
    private StorageReference driversPhotosStorageReference;
    private StorageReference usersPhotosStorageReference;

    public FirebaseAPI(DatabaseReference databaseReference, StorageReference storageReference){//, AreasHelper areasHelper){
        this.databaseReference = databaseReference;
        this.storageReference = storageReference;
        //this.areasHelper = areasHelper;
    }

    public void checkForData(final FirebaseActionListenerCallback listener){//checa se tem dados(era usado no PhotoFeed App para verificar se a lista de fotos estava vazia).
        ValueEventListener postListener = new ValueEventListener() {
            @Override public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
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

    public void updateMyLocation(LatLng location, FirebaseActionListenerCallback listenerCallback) {

        getMyUserReference().child(LATITUDE_PATH).setValue(location.latitude);
        getMyUserReference().child(LONGITUDE_PATH).setValue(location.longitude);

    }

    public void subscribeForNearDriversUpdate(final GroupAreas groupAreas, final FirebaseEventListenerCallback listener){

        if(areasEventListener==null){
            areasEventListener = new ChildEventListener() {
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
                    listener.onChildMoved(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };
            mainAreaDataReference = getAreaDataReference(groupAreas.getMainArea().getId());
            areaVerticalSideDataReference= getAreaDataReference(groupAreas.getAreaVerticalSide().getId());
            areaHorizontalSideDataReference = getAreaDataReference(groupAreas.getAreaHorizontalSide().getId());
            areaDiagonalDataReference = getAreaDataReference(groupAreas.getAreaDiagonal().getId());

            mainAreaDataReference.addChildEventListener(areasEventListener);
            areaVerticalSideDataReference.addChildEventListener(areasEventListener);
            areaHorizontalSideDataReference.addChildEventListener(areasEventListener);
            areaDiagonalDataReference.addChildEventListener(areasEventListener);

        }

    }

    public void unsubscribeForNearDriversUpdates(){
        if(mainAreaDataReference!=null&&areaVerticalSideDataReference!=null&&areaHorizontalSideDataReference!=null&&areaDiagonalDataReference!=null) {
            mainAreaDataReference.removeEventListener(areasEventListener);
            areaVerticalSideDataReference.removeEventListener(areasEventListener);
            areaHorizontalSideDataReference.removeEventListener(areasEventListener);
            areaDiagonalDataReference.removeEventListener(areasEventListener);
        }
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

    public void subscribeForStatusReceiverUpdate(final String receiver, final FirebaseEventListenerCallback listener) {
        if(statusReceiverChatEventListener==null) {
            statusReceiverChatEventListener= new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listener.onChildChanged(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };

            Log.d("d", "getStatusReceiverChatReference(receiver): " + getStatusReceiverChatReference(receiver));
            getStatusReceiverChatReference(receiver).addValueEventListener(statusReceiverChatEventListener);

        }
    }

    public void unSubscribeForStatusReceiverUpdate(String receiver) {
        getStatusReceiverChatReference(receiver).removeEventListener(statusReceiverChatEventListener);
    }

    public void setMessageChatRead(String receiver, String msgId) {
        getChatsReference(receiver).child(msgId).child("read").setValue(true);
    }

    public String createChatMessageId(String receiver) {
        return getChatsReference(receiver).push().getKey();
    }

    public void setChatReceiver(String chatReceiver) {
        getChatsReference(chatReceiver).child(DRIVER_PATH).setValue(chatReceiver);
        getChatsReference(chatReceiver).child(USERS_PATH).setValue(getAuthUserEmail());
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

    public void getUrlPhotoDriver(String email, final FirebaseEventListenerCallback listenerCallback) {
        ValueEventListener urlPhotoDriverEventListener = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listenerCallback.onChildAdded(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onCancelled(databaseError);
            }
        } ;
        getDriverReference(email).child(URL_PHOTO_DRIVER_PATH).addListenerForSingleValueEvent(urlPhotoDriverEventListener);
    }

    public DatabaseReference getMyHistoricChatsReference(){
        return getHistoriChatsReferenceOfUser(getAuthUserEmail());
    }

    public DatabaseReference getHistoriChatsReferenceOfUser(String email){
        return getUserReference(email).child(HISTORICCHATS_PATH);
    }

    private DatabaseReference getAreaDataReference(String id) {
        DatabaseReference areaReference = null;
        if(id!=null){
            areaReference = databaseReference.getRoot().child(AREAS_PATH).child(id);
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

        String keyChat = keyReceiver + SEPARATOR + keySender;
        /*if (keySender.compareTo(keyReceiver) > 0) {//Esse método retorna um numero inteiro. Se ele for menor do que zero, o primeiro argumento é "menor" (alfabeticamente, nesse caso) que o segundo; maior que zero se o primeiro for "maior" que o segundo, e igual a zero se eles forem iguais. Esse método diferencia maiúsculas de minúsuclas. Se não quiser isso, use o compareToIgnoreCase
            keyChat = keyReceiver + SEPARATOR + keySender;//sempre o primeiro em ordem alfabetica vem primeiro
        }*/
        return databaseReference.getRoot().child(CHATS_PATH).child(keyChat);
    }

    public DatabaseReference getStatusReceiverChatReference(String receiver){
        String keyReceiver = receiver.replace(".","_");
        return databaseReference.getRoot().child(DRIVER_PATH).child(keyReceiver).child(STATUS_PATH);
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
                    DatabaseReference reference = getOneContactReferenceofDriver(email, myEmail);//pegando a
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



    public NearDriver[] getNearDrivers(GroupAreas groupAreas, double latitude, double longitude) {
        return new NearDriver[10];
    }

    public DatabaseReference getNearDriversReference(String email){
        return getUserReference(email).child(NEAR_DRIVERS_PATH);
    }

    public DatabaseReference getMyUrlPhotoReference() {
        return getMyUserReference().child(URL_PHOTO_USER_PATH);
    }

    /*public DatabaseReference getMyNearDriversReference(){
        return getHistoriChatsReferenceOfUser((getAuthUserEmail()));
    }*/

    public void updateAvatarPhoto(final Uri selectedImageUri, final FirebaseStorageFinishedListener firebaseStorageFinishedListener) {//,(String previousImageUrl,

        //final StorageReference photoRef = getUsersPhotosStorageReference().child(selectedImageUri.getLastPathSegment());

        StorageReference photoRef = getUsersPhotosStorageReference().child(getAuthUserEmail().replace(".","_"));

        //StorageReference photoDeleteRef = getUsersPhotosStorageReference().child(previousImageUrl);

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

        /*photoDeleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseStorageFinishedListener.onError(e.getMessage());
            }
        });*/


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

    public void destroyDriversListener() {
        areasEventListener = null;
    }

    public void destroyHistoricChatsListener(){
        historicChatsListEventListener = null;
    }

    public void destroyChatListener() {
        chatEventListener = null;
    }

    public void sendTokenToServer(String token, FirebaseActionListenerCallback listener) {
        String userEmail = getAuthUserEmail();
        if(userEmail!=null) {
            String userEmailKey = userEmail.replace(".","_");
            DatabaseReference myTokenReference = databaseReference.getRoot()
                    .child(NOTIFICATION_TOKEN_PATH)
                    .child(userEmailKey)
                    .child(TOKEN_PATH);
            myTokenReference.setValue(token);
            listener.onSuccess();
        }else{
            listener.onError(null);
        }

    }


}
