package com.rsm.yuri.projecttaxilivre.lib.di;

import com.rsm.yuri.projecttaxilivre.lib.GreenRobotEventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Module
public class LibsModule {

    @Provides
    @Singleton
    EventBus providesEventBus(){
        return new GreenRobotEventBus();
    }



}
