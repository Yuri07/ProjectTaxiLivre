package com.rsm.yuri.projecttaxilivre.login.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivre.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
