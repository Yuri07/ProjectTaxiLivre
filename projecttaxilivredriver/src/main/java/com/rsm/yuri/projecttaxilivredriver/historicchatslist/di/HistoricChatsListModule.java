package com.rsm.yuri.projecttaxilivredriver.historicchatslist.di;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListInteractor;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListPresenter;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListPresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListRepository;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListSessionInteractor;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.HistoricChatsListSessionInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.ConnectivityListener;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.HistoricChatsListView;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.OnItemClickListener;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.adapters.HistoricChatsListAdapter;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

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
    private ConnectivityListener connectivityListener;

    public HistoricChatsListModule(HistoricChatsListView view,
                                   OnItemClickListener clickListener,
                                   ConnectivityListener connectivityListener) {
        this.view = view;
        this.clickListener = clickListener;
        this.connectivityListener = connectivityListener;
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
    HistoricChatsListAdapter providesHistoricChatsListAdapter(List<User> historicChatsList,
                                                              ImageLoader imageLoader,
                                                              OnItemClickListener clickListener,
                                                              ConnectivityListener connectivityListener){
        return new HistoricChatsListAdapter(historicChatsList, imageLoader, clickListener, connectivityListener);
    }

    @Provides
    @Singleton
    OnItemClickListener providesOnItemClickListener(){
        return clickListener;
    }

    @Provides
    @Singleton
    ConnectivityListener providesConnectivityListener(){
        return connectivityListener;
    }

    @Provides
    @Singleton
    List<User> providesDriversList(){
        return new ArrayList<User>();
    }

}
