package com.rsm.yuri.projecttaxilivredriver.home.di;

import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.home.HomeInteractor;
import com.rsm.yuri.projecttaxilivredriver.home.HomeInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.home.HomePresenter;
import com.rsm.yuri.projecttaxilivredriver.home.HomePresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.home.HomeRepository;
import com.rsm.yuri.projecttaxilivredriver.home.HomeRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.home.models.AreasFortalezaHelper;
import com.rsm.yuri.projecttaxilivredriver.home.models.AreasHelper;
import com.rsm.yuri.projecttaxilivredriver.home.models.AreasTeresinaHelper;
import com.rsm.yuri.projecttaxilivredriver.home.ui.HomeView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 09/03/2018.
 */
@Module
public class HomeModule {

    private HomeView view;
    private AreasHelper areasHelper = null;
    private String cidade;

    public HomeModule(HomeView view, String cidade) {
        this.view = view;
        this.cidade = cidade;
    }

    /*public void setAreasHelper(AreasHelper areasHelper){
        this.areasHelper = areasHelper;
    }*/

    @Provides
    @Singleton
    HomeView providesHomeView(){
        return view;
    }

    @Provides
    @Singleton
    HomePresenter providesHomePresenter(EventBus eventBus, HomeView homeView, HomeInteractor homeInteractor){
        return new HomePresenterImpl(homeView, homeInteractor, eventBus);
    }

    @Provides
    @Singleton
    HomeInteractor providesHomeInteractor(HomeRepository homeRepository){
        return new HomeInteractorImpl(homeRepository);
    }

    @Provides
    @Singleton
    HomeRepository providesHomeRepository(FirebaseAPI helper, EventBus eventBus, AreasHelper areasHelper){
        return new HomeRepositoryImpl(helper, eventBus, areasHelper, cidade);
    }

    @Provides
    @Singleton
    AreasHelper providesAreasHelper(){
        String nomeDaClasse = "com.rsm.yuri.projecttaxilivredriver.home.models.Areas" + cidade +"Helper";
//        String nomeDaClasse = "com.rsm.yuri.projecttaxilivredriver.home.models.AreasTeresinaHelper";
//        String nomeDaClasse = "com.rsm.yuri.projecttaxilivredriver.home.models.AreasFortalezaHelper";
//        Log.d("d", "HomeModule - providesAreasHelper() - nome da classe: " + nomeDaClasse);
        Class classe = null;//carregar a classe com o nome da String passada
        try {
            classe = Class.forName(nomeDaClasse);
            areasHelper = (AreasHelper) classe.newInstance();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return areasHelper;
    }

}
