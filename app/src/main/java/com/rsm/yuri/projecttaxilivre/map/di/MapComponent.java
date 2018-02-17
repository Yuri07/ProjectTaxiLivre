package com.rsm.yuri.projecttaxilivre.map.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivre.map.ui.MapFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 22/01/2018.
 */
@Singleton
@Component(modules = {MapModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface MapComponent {
    void inject(MapFragment fragment);
}
