package com.rsm.yuri.projecttaxilivredriver.FirebaseService.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.FirebaseService.MyFirebaseInstanceIDService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 25/04/2018.
 */
@Singleton
@Component(modules = {DomainModule.class, TaxiLivreDriverAppModule.class})
public interface FIIDServiceComponent {
    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);
}
