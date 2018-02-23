package com.rsm.yuri.projecttaxilivre.lib.di;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.rsm.yuri.projecttaxilivre.lib.GlideImageLoader;
import com.rsm.yuri.projecttaxilivre.lib.GreenRobotEventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Module
public class LibsModule {

    private Fragment fragment;
    private Context context;

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setContext(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    EventBus providesEventBus(){
        return new GreenRobotEventBus();
    }

    @Provides
    @Singleton
    ImageLoader providesImageLoader() {
        GlideImageLoader imageLoader = new GlideImageLoader();
        if (context != null) {
            imageLoader.setLoaderContext(context);
        }
        return imageLoader;
    }

    @Provides
    @Singleton
    ImageStorage providesImageStorage(Context context, EventBus eventBus) {
        ImageStorage imageStorage = null;//new CloudinaryImageStorage(context, eventBus);
        return imageStorage;
    }

    @Provides
    @Singleton
    Fragment providesFragment(){
        return this.fragment;
    }

}
