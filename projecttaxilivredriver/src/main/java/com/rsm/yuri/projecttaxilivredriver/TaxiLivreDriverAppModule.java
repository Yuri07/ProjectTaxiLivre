package com.rsm.yuri.projecttaxilivredriver;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 02/03/2018.
 */
@Module
public class TaxiLivreDriverAppModule {

    Application application;
    private final static String SHARED_PREFERENCES_NAME = "UserPrefs";

    public TaxiLivreDriverAppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application){
        return application.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /*@Provides
    @Singleton
    Context providesContext(){
        return application.getApplicationContext();
    }*/

    @Provides
    @Singleton
    Application providesApplication(){
        return application;
    }

}
