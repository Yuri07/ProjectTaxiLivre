package com.rsm.yuri.projecttaxilivre.profile.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;
import com.rsm.yuri.projecttaxilivre.profile.ui.ProfileActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 27/02/2018.
 */
@Singleton
@Component(modules = {ProfileModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface ProfileComponent {
    void inject(ProfileActivity activity);
}
