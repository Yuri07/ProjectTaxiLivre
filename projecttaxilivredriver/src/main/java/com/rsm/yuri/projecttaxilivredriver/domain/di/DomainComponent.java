package com.rsm.yuri.projecttaxilivredriver.domain.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Singleton
@Component(modules = {DomainModule.class, TaxiLivreDriverAppModule.class})
public interface DomainComponent {
}
