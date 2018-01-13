package com.rsm.yuri.projecttaxilivre.lib.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Singleton
@Component(modules = {LibsModule.class, TaxiLivreAppModule.class})
public interface LibsComponent {
}
