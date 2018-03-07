package com.rsm.yuri.projecttaxilivredriver.historicchatslist.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.HistoricChatsListActivity;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 13/01/2018.
 */
@Singleton
@Component(modules = {HistoricChatsListModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface HistoricChatsListComponent {

    void inject(HistoricChatsListActivity activity);

}
