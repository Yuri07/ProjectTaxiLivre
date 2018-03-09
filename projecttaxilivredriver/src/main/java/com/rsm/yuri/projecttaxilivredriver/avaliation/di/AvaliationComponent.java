package com.rsm.yuri.projecttaxilivredriver.avaliation.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationFragment;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 07/03/2018.
 */
@Singleton
@Component(modules = {AvaliationModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface AvaliationComponent {
    void inject(AvaliationFragment fragment);
}
