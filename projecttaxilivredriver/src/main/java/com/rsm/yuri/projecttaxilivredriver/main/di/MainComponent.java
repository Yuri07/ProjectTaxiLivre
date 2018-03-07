package com.rsm.yuri.projecttaxilivredriver.main.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 15/01/2018.
 */
@Singleton
@Component(modules = {MainModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
