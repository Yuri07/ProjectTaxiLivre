package com.rsm.yuri.projecttaxilivre.main.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivre.main.di.MainModule;
import com.rsm.yuri.projecttaxilivre.main.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 15/01/2018.
 */
@Singleton
@Component(modules = {MainModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
