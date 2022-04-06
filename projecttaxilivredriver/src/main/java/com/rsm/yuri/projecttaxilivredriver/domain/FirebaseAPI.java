package com.rsm.yuri.projecttaxilivredriver.domain;

import android.net.Uri;
//import android.support.annotation.NonNull;
import android.util.Log;

import androidx.annotation.NonNull;

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
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.home.entities.NearDriver;
import com.rsm.yuri.projecttaxilivredriver.home.models.GroupAreas;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

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
    private final static String STATUS_PATH = "status";
    private final static String URL_PHOTO_USER_PATH = "urlPhotoUser";
    private final static String URL_PHOTO_DRIVER_PATH = "urlPhotoDriver";
    private final static String URL_PHOTO_TRAVEL_MAP_PATH = "urlPhotoMap";
    private final static String URL_PHOTO_CAR_PATH = "urlPhotoCar";
    private final static String NEAR_DRIVERS_PATH = "neardrivers";
    private final static String AREAS_PATH = "areas";
    private final static String CIDADES_PATH = "cidades";
    private final static String CHATS_PATH = "chats";
    private final static String HISTORICCHATS_PATH = "historicchats";
    private final static String HISTORICTRAVELS_PATH = "historictravels";
    private final static String TRAVELS_PATH = "travels";
    private final static String TRAVELS_BY_DRIVERS_PATH = "bydriverskey";
    private final static String TRAVELS_BY_USERS_PATH = "byuserskey";
    private final static String INITIATE_TRAVEL_PATH = "initiateTravel";
    private final static String TERMINATE_TRAVEL_PATH = "terminateTravel";
    private final static String TRAVEL_ACK_PATH = "travelAck";
    private final static String TRAVEL_ACK_CHILD_PATH = "newTravelID";

    private final static String LOCATION_PATH = "location";
    private final static String RATINGS_PATH = "ratings";
    private final static String USERS_RATINGS_PATH = "usersRatings";
    private final static String LATITUDE_PATH = "latitude";
    private final static String LONGITUDE_PATH = "longitude";
    private final static String SEPARATOR = "___";

    final static String NOTIFICATION_TOKEN_PATH = "notificationToken";
    final static String TOKEN_PATH = "token";

    private static final String DRIVERS_PHOTOS_PATH = "drivers_photos";
    private static final String USERS_PHOTOS_PATH = "users_photos";
    private static final String CARS_PHOTOS_PATH = "cars_photos";

    private static final String  TRAVEL_NOT_ACCEPTED_ACK = "travelNotAcceptedAck";

    //private AreasFortalezaHelper areasHelper;

    private final DatabaseReference databaseReference;

    private ChildEventListener historicChatsListEventListener;
    private ChildEventListener historicTravelsListEventListener;
    private ChildEventListener chatEventListener;

    private ValueEventListener ratingsEventListener;
    private ValueEventListener carEventListener;
    private ValueEventListener statusReceiverChatEventListener;

    private StorageReference storageReference;
    private StorageReference driversPhotosStorageReference;
    private StorageReference usersPhotosStorageReference;

    private FirebaseAuth firebaseAuth;

    public FirebaseAPI(DatabaseReference databaseReference, StorageReference storageReference, FirebaseAuth firebaseAuth){//, AreasFortalezaHelper areasHelper){
        this.databaseReference = databaseReference;
        this.storageReference = storageReference;
        this.firebaseAuth = firebaseAuth;
        //this.areasHelper = areasHelper;
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

    public void subscribeForHistoricTravelsListUpdates(final FirebaseEventListenerCallback listener){

        if(historicTravelsListEventListener==null) {
            historicTravelsListEventListener= new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    listener.onChildAdded(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //listener.onChildChanged(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    //listener.onChildRemoved(dataSnapshot);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    listener.onCancelled(databaseError);
                }
            };

            getMyHistoricTravelsReference().addChildEventListener(historicTravelsListEventListener);

        }

    }

    public void unSubscribeForHistoricTravelsListUpdates() {
        if(historicTravelsListEventListener!=null) {
            getMyHistoricTravelsReference().removeEventListener(historicTravelsListEventListener);
        }
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

    public void uploadNearDriverData(final NearDriver nearDriver, final String cidade, final String idArea,final FirebaseActionListenerCallback listenerCallback) {
        getAreaDataReference(idArea, cidade).child(nearDriver.getEmail()
                .replace(".","_")).setValue(nearDriver, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    listenerCallback.onSuccess();
                }else{
                    listenerCallback.onError(databaseError);
                }
            }
        });
    }

    public String acceptTravel(Travel travel, String cidade, String idArea){
        Log.d("d", "FirebaseAPI.acceptTravel()");
        getAreaDataReference(idArea, cidade).child(travel.getDriverEmail().replace(".","_")).removeValue();

        Log.d("d", "FirebaseAPI.acceptTravel() - remove driver da area");
        String newTravelId = createTravelId(travel.getRequesterEmail());

        Log.d("d", "FirebaseAPI.acceptTravel() - newTravelId: " + newTravelId);
        travel.setTravelId(newTravelId);

        HistoricTravelItem historicTravelItem = new HistoricTravelItem(travel);

        getTravelsReference(travel.getRequesterEmail()).child(newTravelId).setValue(travel);

        getDriverReference(travel.getDriverEmail()).child(HISTORICTRAVELS_PATH).child(newTravelId)
                .setValue(historicTravelItem);

        getUserReference(travel.getRequesterEmail()).child(HISTORICTRAVELS_PATH).child(newTravelId)
                .setValue(historicTravelItem);

//        getUserReference(travel.getRequesterEmail()).child(TRAVEL_ACK_PATH)
//                        .child(TRAVEL_ACK_CHILD_PATH).setValue(newTravelId);

        Map<String, Object> update = new HashMap<String, Object>();
        update.put(TRAVEL_ACK_CHILD_PATH, newTravelId);
//        getUserReference(travel.getRequesterEmail()).child(TRAVEL_ACK_PATH).updateChildren(update);

        return newTravelId;

    }

    public void notifyRequesterTravelNotAccepted(String requesterEmail){
        getUserReference(requesterEmail).child(TRAVEL_ACK_PATH)
                .child(TRAVEL_ACK_CHILD_PATH).setValue(TRAVEL_NOT_ACCEPTED_ACK);
    }

    public String createTravelId(String requesterEmail) {
        //return getTravelByDriverKeyReference(getAuthUserEmail().replace(".","_")).push().getKey();
        return getTravelByDriverKeyReference(requesterEmail.replace(".","_")).push().getKey();

    }

    public DatabaseReference  getTravelByDriverKeyReference(String emailDriverKey){
        return getTravelsReference(emailDriverKey);
        //return databaseReference.getRoot().child(TRAVELS_PATH).child(TRAVELS_BY_DRIVERS_PATH).child(emailDriverKey);
    }

    public void updateMyLocation(final LatLng location, final String cidade, final String idArea, final FirebaseActionListenerCallback listenerCallback){
        String authUserEmail = getAuthUserEmail();
        //GroupAreas groupAreas = areasHelper.getGroupAreas(location.latitude, location.longitude);
        //NearDriver nearDriver = new NearDriver(getAuthUserEmail(),location.latitude, location.longitude );
        final String emailKey = authUserEmail.replace(".","_");
        getAreaDataReference(idArea, cidade).child(emailKey).
                child(LATITUDE_PATH).setValue(location.latitude, new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
                            getAreaDataReference(idArea, cidade).child(emailKey).
                                        child(LONGITUDE_PATH).setValue(location.longitude);
                            getMyUserReference().child(LATITUDE_PATH).setValue(location.latitude);
                            getMyUserReference().child(LONGITUDE_PATH).setValue(location.longitude);
                            listenerCallback.onSuccess();
                        }else{
                            listenerCallback.onError(databaseError);
                        }
                    }
                });
    }

    public void updateMyLocationForTravel(final LatLng location, final String requesterEmail, final String newTravelID,
                                          final FirebaseActionListenerCallback listenerCallback){
        //String authUserEmail = getAuthUserEmail();
        getTravelsReference(requesterEmail).
                child(newTravelID).child("latDriver").setValue(location.latitude, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){
                    getTravelsReference(requesterEmail).child(newTravelID).child("longDriver").setValue(location.longitude);
                    listenerCallback.onSuccess();
                }else{
                    listenerCallback.onError(databaseError);
                }
            }
        });

    }

    public void initiateTravel(String emailRequester, String travelID) {
        getTravelsReference(emailRequester).child(travelID).child(INITIATE_TRAVEL_PATH)
                .setValue("true");
    }

    public void terminateTravel(String emailRequester, String travelID) {
        getTravelsReference(emailRequester).child(travelID).child(TERMINATE_TRAVEL_PATH)
                .setValue("true");
    }

    public void uploadUserRating(final String userEmail, final Rating rating){
        getUserRatingsReference(userEmail).child(rating.getTravelId()).setValue(rating);

        getUserReference(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            String nCountStars;
            int nCountStarsValue;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User passenger = dataSnapshot.getValue(User.class);
                if(passenger!=null) {
                    switch ((int) rating.getVote()) {
                        case 1:
                            nCountStars = "countStars1";
                            passenger.setCount1Stars(passenger.getCount1Stars() + 1);
                            nCountStarsValue = passenger.getCount1Stars() + 1;
                            passenger.setAverageRating(passenger.getAverageRating());
                            //getUserReference(userEmail).child("countStars1").setValue(passenger.getCount1Stars());
                            //getUserReference(userEmail).child("countStars1")
                            break;
                        case 2:
                            nCountStars = "countStars2";
                            passenger.setCount2Stars(passenger.getCount2Stars() + 1);
                            nCountStarsValue = passenger.getCount2Stars() + 1;
                            break;
                        case 3:
                            nCountStars = "countStars3";
                            passenger.setCount3Stars(passenger.getCount3Stars() + 1);
                            nCountStarsValue = passenger.getCount3Stars() + 1;
                            break;
                        case 4:
                            nCountStars = "countStars4";
                            passenger.setCount4Stars(passenger.getCount4Stars() + 1);
                            nCountStarsValue = passenger.getCount4Stars() + 1;
                            break;
                        case 5:
                            nCountStars = "countStars5";
                            passenger.setCount5Stars(passenger.getCount5Stars() + 1);
                            nCountStarsValue = passenger.getCount5Stars() + 1;
                            break;
                    }

                    passenger.setTotalRatings(passenger.getTotalRatings()+1);
                    double averageRatings = passenger.getCount1Stars() + passenger.getCount2Stars() * 2
                            + passenger.getCount3Stars() * 3 + passenger.getCount4Stars() * 4 + passenger.getCount5Stars() * 5;
                    int totalRatings = passenger.getTotalRatings();

                    HashMap<String, Object> ratingValues = new HashMap<>();
                    ratingValues.put(nCountStars, nCountStarsValue);
                    ratingValues.put("averageRating", averageRatings);
                    ratingValues.put("totalRatings", totalRatings);

                    String userEmailKey = userEmail.replace("." ,"_");

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/" + userEmailKey, ratingValues);
                    databaseReference.updateChildren(childUpdates);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void saveCity(String city){
        DatabaseReference myUserReference = getMyUserReference();
        if(myUserReference!=null)
            myUserReference.child("cidade").setValue(city);
            //getMyUserReference().child("cidade").setValue(city);

    }

    public void removeDriverFromArea(String cidade, GroupAreas groupAreas, final FirebaseActionListenerCallback listenerCallback) {
        //NearDriver nearDriver = new NearDriver();
        String authUserEmail = getAuthUserEmail();
        getAreaDataReference(groupAreas.getMainArea().getId(), cidade).child(authUserEmail.replace(".","_"))
                .setValue(null, new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null){
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
        childUpdates.put("/"+CAR_PATH+"/"   + car.getEmail().replace(".","_")                  , carValues       );

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
        getUserReference(email).child(URL_PHOTO_USER_PATH).addListenerForSingleValueEvent(urlPhotoDriverEventListener);
    }

    public void getUrlPhotoMapTravel(String travelId, final FirebaseEventListenerCallback listenerCallback) {
        ValueEventListener urlPhotoTravelMapEventListener = new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listenerCallback.onChildAdded(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listenerCallback.onCancelled(databaseError);
            }
        } ;
        getDriverReference(getAuthUserEmail()).child(HISTORICTRAVELS_PATH)
                .child(travelId).child(URL_PHOTO_TRAVEL_MAP_PATH)
                .addListenerForSingleValueEvent(urlPhotoTravelMapEventListener);
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

    public DatabaseReference getUserRatingsReference(String userEmaiil){
        //return getRatingsReference(getAuthUserEmail());
        DatabaseReference ratingsReference = null;
        if(userEmaiil!=null){
            String emailKey = userEmaiil.replace(".","_");
            ratingsReference = databaseReference.getRoot().child(USERS_RATINGS_PATH).child(emailKey);
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

    public DatabaseReference getMyHistoricTravelsReference(){
        return getHistoriTravelsReferenceOfDriver(getAuthUserEmail());
    }

    public DatabaseReference getHistoriChatsReferenceOfDriver(String email){
        return getDriverReference(email).child(HISTORICCHATS_PATH);
    }

    public DatabaseReference getHistoriTravelsReferenceOfDriver(String email){
        return getDriverReference(email).child(HISTORICTRAVELS_PATH);
    }

    public DatabaseReference getHistoriChatsReferenceOfUser(String email){
        return getUserReference(email).child(HISTORICCHATS_PATH);
    }

    private DatabaseReference getAreaDataReference(String idArea, String cidade) {
        DatabaseReference areaReference = null;
        if(idArea!=null){
            areaReference = databaseReference.getRoot().child(CIDADES_PATH).child(cidade).child(AREAS_PATH).child(idArea);
        }
        return areaReference;

    }

    public void checkForSession(FirebaseActionListenerCallback listener) {
        FirebaseUser user = firebaseAuth.getCurrentUser();//FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            listener.onSuccess();
        } else {
            listener.onError(null);
        }
    }

    public void logout() {
        firebaseAuth.signOut();
        //FirebaseAuth.getInstance().signOut();
        //notifyContactsOfConnectionChange(User.OFFLINE, true);
        changeUserConnectionStatus(Driver.OFFLINE);
    }

    public DatabaseReference getMyUserReference(){
        return getDriverReference(getAuthUserEmail());
    }

    public String getAuthUserEmail(){
        FirebaseUser user = firebaseAuth.getCurrentUser();//FirebaseAuth.getInstance().getCurrentUser();
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

        String keyChat = keySender + SEPARATOR + keyReceiver;//essa linha é invertida no app do usuario

        /*if (keySender.compareTo(keyReceiver) > 0) {//Esse método retorna um numero inteiro. Se ele for menor do que zero, o primeiro argumento é "menor" (alfabeticamente, nesse caso) que o segundo; maior que zero se o primeiro for "maior" que o segundo, e igual a zero se eles forem iguais. Esse método diferencia maiúsculas de minúsuclas. Se não quiser isso, use o compareToIgnoreCase
            keyChat = keyReceiver + SEPARATOR + keySender;//sempre o primeiro em ordem alfabetica vem primeiro
        }*/
        return databaseReference.getRoot().child(CHATS_PATH).child(keyChat);
    }

    public DatabaseReference getTravelsReference(String userEmail){
        String keyDriverEmail = getAuthUserEmail().replace(".","_");
        String keyUserEmail = userEmail.replace(".","_");

        String keyTravel = keyDriverEmail + SEPARATOR + keyUserEmail;
        /*if (keySender.compareTo(keyReceiver) > 0) {//Esse método retorna um numero inteiro. Se ele for menor do que zero, o primeiro argumento é "menor" (alfabeticamente, nesse caso) que o segundo; maior que zero se o primeiro for "maior" que o segundo, e igual a zero se eles forem iguais. Esse método diferencia maiúsculas de minúsuclas. Se não quiser isso, use o compareToIgnoreCase
            keyChat = keyReceiver + SEPARATOR + keySender;//sempre o primeiro em ordem alfabetica vem primeiro
        }*/
        return databaseReference.getRoot().child(TRAVELS_PATH).child(keyTravel);
    }

    public DatabaseReference getStatusReceiverChatReference(String receiver){
        String keyReceiver = receiver.replace(".","_");
        return databaseReference.getRoot().child(USERS_PATH).child(keyReceiver).child(STATUS_PATH);
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
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                //myUrlPhotoReference.setValue(downloadUrl.toString());
                //firebaseStorageFinishedListener.onSuccess(downloadUrl.toString());
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        myUrlPhotoReference.setValue(downloadUrl.toString());
                        firebaseStorageFinishedListener.onSuccess(downloadUrl.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseStorageFinishedListener.onError(e.getMessage());
            }
        });

    }

    public DatabaseReference getMyCarUrlPhotoReference() {
        return getMyCarReference().child(URL_PHOTO_CAR_PATH);
    }

    public void updateCarPhoto(final Uri selectedImageUri, final FirebaseStorageFinishedListener firebaseStorageFinishedListener) {//,(String previousImageUrl,

        StorageReference photoRef = getCarsPhotosStorageReference().child(getAuthUserEmail().replace(".","_"));

        final DatabaseReference myCarUrlPhotoReference = getMyCarUrlPhotoReference();

        photoRef.putFile(selectedImageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                //myCarUrlPhotoReference.setValue(downloadUrl.toString());
                //firebaseStorageFinishedListener.onSuccess(downloadUrl.toString());
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String downloadUrl = uri.toString();
                        myCarUrlPhotoReference.setValue(downloadUrl.toString());
                        firebaseStorageFinishedListener.onSuccess(downloadUrl.toString());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseStorageFinishedListener.onError(e.getMessage());
            }
        });

    }

    public void uploadMapPhoto(final Uri selectedImageUri, final FirebaseStorageFinishedListener firebaseStorageFinishedListener) {//,(String previousImageUrl,

        StorageReference photoRef = getDriversPhotosStorageReference().child(getAuthUserEmail().replace(".","_"));

        final DatabaseReference myUrlPhotoReference = getMyUrlPhotoReference();

        photoRef.putFile(selectedImageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
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

    public StorageReference getCarsPhotosStorageReference(){
        return storageReference.getRoot().child(CARS_PHOTOS_PATH);
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

    public void destroyHistoricTravelsListener(){
        historicTravelsListEventListener = null;
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
