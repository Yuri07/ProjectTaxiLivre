package com.rsm.yuri.projecttaxilivredriver.profile.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivredriver.profile.ui.ProfileFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 27/02/2018.
 */
@Singleton
@Component(modules = {ProfileModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface ProfileComponent {
    void inject(ProfileFragment fragment);
}
