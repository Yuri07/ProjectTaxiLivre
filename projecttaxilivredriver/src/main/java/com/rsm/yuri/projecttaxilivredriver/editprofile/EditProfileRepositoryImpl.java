package com.rsm.yuri.projecttaxilivredriver.editprofile;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverApp;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseActionListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseEventListenerCallback;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseStorageFinishedListener;
import com.rsm.yuri.projecttaxilivredriver.editprofile.events.EditProfileEvent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.main.events.MainEvent;

/**
 * Created by yuri_ on 14/03/2018.
 */

public class EditProfileRepositoryImpl implements EditProfileRepository {

    private EventBus eventBus;
    private FirebaseAPI firebase;
    private UploaderPhoto uploaderPhoto;

    private DatabaseReference myUserReference;

    public EditProfileRepositoryImpl(EventBus eventBus, FirebaseAPI firebase, UploaderPhoto uploaderPhoto) {
        this.eventBus = eventBus;
        this.firebase = firebase;
        this.uploaderPhoto = uploaderPhoto;
    }

    @Override
    public void saveProfile(final Driver driver, final Car car) {
        //updateKeyValueDriver(TaxiLivreDriverApp.NOME_KEY, driver.getNome());
        //updateCar(car);
        firebase.updateProfile(driver, car, new FirebaseActionListenerCallback(){

            @Override
            public void onSuccess() {
                post(EditProfileEvent.SAVE_PROFILE_SUCESS, driver, car);
            }

            @Override
            public void onError(DatabaseError error) {
                post(EditProfileEvent.SAVE_PROFILE_ERROR, error.getMessage());
            }
        });
    }

    @Override
    public void uploadPhoto(Uri selectedImageUri) {
        uploaderPhoto.uploadPhoto(selectedImageUri);
    }

    @Override
    public void retrieveDataUser() {
        firebase.checkForSession(new FirebaseActionListenerCallback(){
            @Override
            public void onSuccess() {

                myUserReference = firebase.getMyUserReference();//pega a referencia no
                // database para o usuario atualmente autenticado

                //myUserReference.keepSynced(true);

                myUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        postOnSuccessRetrieveDataUser(snapshot);
                    }
                    @Override
                    public void onCancelled(DatabaseError firebaseError) {
                        post(EditProfileEvent.onFailedToGetDateUser, firebaseError.getMessage());
                    }
                });
            }

            @Override
            public void onError(DatabaseError error) {
                post(EditProfileEvent.onFailedToGetDateUser,"Falha em recuperar dados do usuario");
            }
        });
    }

    /*@Override
    public void uploadPhotoDriver(Uri selectedImageUri) {
        //uploaderPhoto.uploadPhoto(selectedImageUri);
        f(selectedImageUri!=null) {
            Log.d("d", "post(url, ProfileEventUPLOAD_INIT)");
            post(EditProfileEvent.UPLOAD_INIT);

            firebase.updateAvatarPhoto(selectedImageUri, new FirebaseStorageFinishedListener() {

                @Override
                public void onSuccess(String url) {
                    Log.d("d", "post(url, ProfileEventUPLOAD_COMPLETE)");
                    post(url, EditProfileEvent.UPLOAD_COMPLETE);
                }

                @Override
                public void onError(String error) {
                    post(EditProfileEvent.UPLOAD_ERROR, error);
                }
            });
        }

    }*/

    /*@Override
    public void uploadPhotoCar(Uri selectedImageUri) {
        //uploaderPhoto.uploadPhoto(selectedImageUri);
        if(selectedImageUri!=null) {
            Log.d("d", "post(url, ProfileEventUPLOAD_INIT)");
            post(EditProfileEvent.UPLOAD_INIT);

            firebase.updateCarPhoto(selectedImageUri, new FirebaseStorageFinishedListener() {

                @Override
                public void onSuccess(String url) {
                    Log.d("d", "post(url, ProfileEventUPLOAD_COMPLETE)");
                    post(url, EditProfileEvent.UPLOAD_COMPLETE);
                }

                @Override
                public void onError(String error) {
                    post(EditProfileEvent.UPLOAD_ERROR, error);
                }
            });
        }

    }*/

    @Override
    public void updateDriver(Driver driver) {

    }

    @Override
    public void updateCar(Car car) {

    }

    private void postOnSuccessRetrieveDataUser(DataSnapshot snapshot){
        Driver currentUser = snapshot.getValue(Driver.class);//null caso seja a primeira vez que o metodo e executado para esse usuario
        firebase.getMyCar(new FirebaseEventListenerCallback() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot) {
                Car car = dataSnapshot.getValue(Car.class);
                post(EditProfileEvent.onSuccessToGetDateUser, currentUser, car);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                post(EditProfileEvent.onFailedToGetDateUser,"Falha em recuperar dados do carro usuario");
            }
        });

    }

    private void post(int type){
        post(type, null, null, null, null);
    }

    private  void post(String url, int type){
        post(type, null, null, url, null);
    }

    private void post(int type, Driver driver, Car car) {
        Log.d("d", "EditProfileRepository.post(type, driver, car): "+ driver.getEmail());
        post(type, driver, car, null, null);
    }

    private void post(int type, String errorMsg){
        post(type,null, null, null,errorMsg);
    }

    private void post(int type, Driver driver, Car car, String url,String errorMsg){
        Log.d("d", "EditProfileRepository.post(): ");
        EditProfileEvent event = new EditProfileEvent();
        event.setUlrPhoto(url);
        event.setType(type);
        event.setDriver(driver);
        event.setCar(car);
        event.setError(errorMsg);
        eventBus.post(event);
    }

}
