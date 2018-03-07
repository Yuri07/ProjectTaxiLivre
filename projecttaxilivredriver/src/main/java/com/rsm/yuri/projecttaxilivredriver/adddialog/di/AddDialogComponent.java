package com.rsm.yuri.projecttaxilivredriver.adddialog.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.adddialog.ui.AddDialogFragment;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 27/02/2018.
 */
@Singleton
@Component(modules = {AddDialogModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface AddDialogComponent {
    void inject(AddDialogFragment fragment);
}
