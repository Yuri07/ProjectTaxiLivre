package com.rsm.yuri.projecttaxilivre.FirebaseService.di;

import com.rsm.yuri.projecttaxilivre.FirebaseService.MyFirebaseInstanceIDService;
import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 25/04/2018.
 */
@Singleton
@Component(modules = {DomainModule.class, TaxiLivreAppModule.class})
public interface FIIDServiceComponent {
    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);
}
