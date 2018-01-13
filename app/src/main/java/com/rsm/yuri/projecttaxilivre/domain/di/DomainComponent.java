package com.rsm.yuri.projecttaxilivre.domain.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Singleton
@Component(modules = {DomainModule.class, TaxiLivreAppModule.class})
public interface DomainComponent {
}
