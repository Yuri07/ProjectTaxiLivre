package com.rsm.yuri.projecttaxilivre.historicchatslist.di;

import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListInteractor;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListInteractorImpl;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListPresenter;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListPresenterImpl;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListRepository;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListSessionInteractor;
import com.rsm.yuri.projecttaxilivre.historicchatslist.HistoricChatsListSessionInteractorImpl;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.HistoricChatsListView;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.OnItemClickListener;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.adapters.HistoricChatsListAdapter;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 13/01/2018.
 */
@Module
public class HistoricChatsListModule {

    private HistoricChatsListView view;
    private OnItemClickListener clickListener;

    public HistoricChatsListModule(HistoricChatsListView view, OnItemClickListener clickListener) {
        this.view = view;
        this.clickListener = clickListener;
    }

    @Provides
    @Singleton
    HistoricChatsListView providesHistoricChatsListView(){
        return view;
    }

    @Provides
    @Singleton
    HistoricChatsListPresenter providesHistoricChatsListPresenter(EventBus eventBus,
                                                                  HistoricChatsListView historicChatsListView,
                                                                  HistoricChatsListInteractor historicChatsListInteractor, HistoricChatsListSessionInteractor sessionInteractor){
        return new HistoricChatsListPresenterImpl(eventBus,
                historicChatsListView,
                historicChatsListInteractor,
                sessionInteractor);
    }

    @Provides
    @Singleton
    HistoricChatsListInteractor providesHistoricChatsListInteractor(HistoricChatsListRepository historicChatsListRepository){
        return new HistoricChatsListInteractorImpl(historicChatsListRepository);
    }

    @Provides
    @Singleton
    HistoricChatsListSessionInteractor providesHistoricChatsListSessionInteractor(HistoricChatsListRepository historicChatsListRepository){
        return new HistoricChatsListSessionInteractorImpl(historicChatsListRepository);
    }

    @Provides
    @Singleton
    HistoricChatsListRepository providesHistoricChatsListRepository(FirebaseAPI helper, EventBus eventBus){
        return new HistoricChatsListRepositoryImpl(helper, eventBus);
    }

    @Provides
    @Singleton
    HistoricChatsListAdapter providesHistoricChatsListAdapter(List<Driver> historicChatsList, ImageLoader imageLoader, OnItemClickListener clickListener){
        return new HistoricChatsListAdapter(historicChatsList, imageLoader, clickListener);
    }

    @Provides
    @Singleton
    OnItemClickListener providesOnItemClickListener(){
        return clickListener;
    }

    @Provides
    @Singleton
    List<Driver> providesDriversList(){
        return new ArrayList<Driver>();
    }

}
