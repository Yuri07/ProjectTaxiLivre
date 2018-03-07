package com.rsm.yuri.projecttaxilivredriver.chat.di;

import com.rsm.yuri.projecttaxilivredriver.TaxiLivreDriverAppModule;
import com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatActivity;
import com.rsm.yuri.projecttaxilivredriver.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivredriver.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 13/01/2018.
 */
@Singleton
@Component(modules = {ChatModule.class, DomainModule.class, LibsModule.class, TaxiLivreDriverAppModule.class})
public interface ChatComponet {
    void inject(ChatActivity activity);
}
