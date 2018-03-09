package com.rsm.yuri.projecttaxilivredriver.home.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeFragment;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 09/03/2018.
 */
@Singleton
@Component(modules = {HomeModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface HomeComponent {
    void inject(HomeFragment fragment);
}
