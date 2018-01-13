package com.rsm.yuri.projecttaxilivre;

import android.app.Application;

import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class TaxiLivreApp extends Application{

    private final static String EMAIL_KEY = "email";
    private LibsModule libsModule;
    private DomainModule domainModule;
    private TaxiLivreAppModule taxiLivreAppModule;

    @Override
    public void onCreate() {
        super.onCreate();
        initModules();
    }

    private void initModules() {
        libsModule = new LibsModule();
        domainModule = new DomainModule();
        taxiLivreAppModule= new TaxiLivreAppModule(this);
    }

    public static String getEmailKey() {
        return EMAIL_KEY;
    }



}
