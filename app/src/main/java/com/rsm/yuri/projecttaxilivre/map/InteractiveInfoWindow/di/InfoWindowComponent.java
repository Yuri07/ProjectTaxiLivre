package com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivre.map.InteractiveInfoWindow.InfoWindowFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 29/03/2018.
 */
@Singleton
@Component(modules = {InfoWindowModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface InfoWindowComponent {
    void inject(InfoWindowFragment fragment);
}
