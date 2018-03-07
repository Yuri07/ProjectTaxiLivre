package com.rsm.yuri.projecttaxilivredriver.login.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivredriver.login.ui.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Singleton
@Component(modules = {LoginModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
