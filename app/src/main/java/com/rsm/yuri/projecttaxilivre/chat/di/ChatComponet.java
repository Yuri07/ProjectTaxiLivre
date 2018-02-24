package com.rsm.yuri.projecttaxilivre.chat.di;

import com.rsm.yuri.projecttaxilivre.TaxiLivreAppModule;
import com.rsm.yuri.projecttaxilivre.chat.ui.ChatActivity;
import com.rsm.yuri.projecttaxilivre.domain.di.DomainModule;
import com.rsm.yuri.projecttaxilivre.lib.di.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by yuri_ on 13/01/2018.
 */
@Singleton
@Component(modules = {ChatModule.class, DomainModule.class, LibsModule.class, TaxiLivreAppModule.class})
public interface ChatComponet {
    void inject(ChatActivity activity);
}
