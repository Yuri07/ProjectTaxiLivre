package com.rsm.yuri.projecttaxilivredriver.historictravelslist.di;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.HistoricTravelsListInteractor;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.HistoricTravelsListInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.HistoricTravelsListPresenter;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.HistoricTravelsListPresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.HistoricTravelsListRepository;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.HistoricTravelsListRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.entities.HistoricTravelItem;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.HistoricTravelListView;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.OnHistoricTravelItemClickListener;
import com.rsm.yuri.projecttaxilivredriver.historictravelslist.ui.adapters.HistoricTravelsListAdapter;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.lib.base.ImageLoader;
import com.rsm.yuri.projecttaxilivredriver.main.entities.Travel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HistoricTravelsListModule {

    private HistoricTravelListView view;
    private OnHistoricTravelItemClickListener clickListener;

    public HistoricTravelsListModule(HistoricTravelListView view, OnHistoricTravelItemClickListener clickListener) {
        this.view = view;
        this.clickListener = clickListener;
    }

    @Provides
    @Singleton
    HistoricTravelListView providesHistoricTravelListView(){
        return view;
    }

    @Provides
    @Singleton
    HistoricTravelsListPresenter providesHistoricTravelsListPresenter(EventBus eventBus,
                                                                      HistoricTravelListView historicChatsListView,
                                                                      HistoricTravelsListInteractor historicChatsListInteractor){
                                                                      //, HistoricChatsListSessionInteractor sessionInteractor){
        return new HistoricTravelsListPresenterImpl(eventBus,
                historicChatsListView,
                historicChatsListInteractor);//, sessionInteractor);
    }

    @Provides
    @Singleton
    HistoricTravelsListInteractor providesHistoricTravelsListInteractor(HistoricTravelsListRepository historicTravelsListRepository){
        return new HistoricTravelsListInteractorImpl(historicTravelsListRepository);
    }

    /*@Provides
    @Singleton
    HistoricTravelsListSessionInteractor providesHistoricTravelsListSessionInteractor(HistoricTravelsListRepository historicTravelsListRepository){
        return new HistoricTravelsListSessionInteractorImpl(historicTravelsListRepository);
    }*/

    @Provides
    @Singleton
    HistoricTravelsListRepository providesHistoricTravelsListRepository(FirebaseAPI helper, EventBus eventBus){
        return new HistoricTravelsListRepositoryImpl(helper, eventBus);
    }

    @Provides
    @Singleton
    HistoricTravelsListAdapter providesHistoricTravelsListAdapter(List<HistoricTravelItem> historicTravelsList, ImageLoader imageLoader, OnHistoricTravelItemClickListener clickListener){
        return new HistoricTravelsListAdapter(historicTravelsList, imageLoader, clickListener);
    }

    @Provides
    @Singleton
    OnHistoricTravelItemClickListener providesOnItemClickListener(){
        return clickListener;
    }

    @Provides
    @Singleton
    List<HistoricTravelItem> providesTravelsList(){
        return new ArrayList<HistoricTravelItem>();
    }

}
