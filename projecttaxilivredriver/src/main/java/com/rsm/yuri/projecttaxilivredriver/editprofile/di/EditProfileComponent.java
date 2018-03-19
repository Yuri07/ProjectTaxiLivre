package com.rsm.yuri.projecttaxilivredriver.editprofile.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.editprofile.ui.EditProfileActivity;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 14/03/2018.
 */
@Singleton
@Component(modules = {EditProfileModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface EditProfileComponent {
    void inject(EditProfileActivity activity);
}
