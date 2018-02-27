package com.rsm.yuri.projecttaxilivre.adddialog.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.adddialog.ui.AddDialogFragment;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 27/02/2018.
 */
@Singleton
@Component(modules = {AddDialogModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface AddDialogComponent {
    void inject(AddDialogFragment fragment);
}
