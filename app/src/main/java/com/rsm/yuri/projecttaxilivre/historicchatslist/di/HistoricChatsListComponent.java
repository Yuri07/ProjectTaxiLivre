package com.rsm.yuri.projecttaxilivre.historicchatslist.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreApp;
import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.HistoricChatsListActivity;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 13/01/2018.
 */
@Singleton
@Component(modules = {HistoricChatsListModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface HistoricChatsListComponent {

    void inject(HistoricChatsListActivity activity);

}
