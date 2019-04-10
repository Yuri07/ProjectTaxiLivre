package com.rsm.yuri.projecttaxilivredriver.domain.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Module
public class DomainModule {

    public DomainModule() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Provides
    @Singleton
    FirebaseAPI providesFirebaseAPI(DatabaseReference databaseReference, StorageReference storageReference, FirebaseAuth firebaseAuth) {
        return new FirebaseAPI(databaseReference, storageReference, firebaseAuth);
    }

    @Provides
    @Singleton
    DatabaseReference providesFirebase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides
    @Singleton
    StorageReference providesStorageReference(){
        return FirebaseStorage.getInstance().getReference();
    }

    @Provides
    @Singleton
    FirebaseAuth providesFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

}
