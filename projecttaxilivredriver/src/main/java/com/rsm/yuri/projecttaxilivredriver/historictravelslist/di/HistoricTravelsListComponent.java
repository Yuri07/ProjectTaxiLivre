package com.rsm.yuri.projecttaxilivredriver.historictravelslist.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.HistoricTravelListActivity;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 07/12/2018.
 */
@Singleton
@Component(modules = {HistoricTravelsListModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface HistoricTravelsListComponent {

    void inject(HistoricTravelListActivity activity);

}
