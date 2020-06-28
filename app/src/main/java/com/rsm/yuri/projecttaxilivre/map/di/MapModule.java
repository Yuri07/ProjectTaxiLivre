package com.rsm.yuri.projecttaxilivre.map.di;

import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.MapInteractor;
import com.rsm.yuri.projecttaxilivre.map.MapInteractorImpl;
import com.rsm.yuri.projecttaxilivre.map.MapPresenter;
import com.rsm.yuri.projecttaxilivre.map.MapPresenterImpl;
import com.rsm.yuri.projecttaxilivre.map.MapRepository;
import com.rsm.yuri.projecttaxilivre.map.MapRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.map.models.AreasFortalezaHelper;
import com.rsm.yuri.projecttaxilivre.map.models.AreasHelper;
import com.rsm.yuri.projecttaxilivre.map.models.AreasTeresinaHelper;
import com.rsm.yuri.projecttaxilivre.map.ui.MapView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 22/01/2018.
 */
@Module
public class MapModule {

    MapView mapView;
    private AreasHelper areasHelper = null;
    String cidade;

    public MapModule(MapView mapView, String cidade){
        this.mapView = mapView;
        this.cidade = cidade;
    }

    @Provides
    @Singleton
    MapView providesMapView(){
        return  mapView;
    }

    @Provides
    @Singleton
    MapPresenter providesMapPresenter(EventBus eventBus, MapView mapView, MapInteractor mapInteractor){
        return new MapPresenterImpl(eventBus, mapView, mapInteractor);
    }

    @Provides
    @Singleton
    MapInteractor providesMapInteractor(MapRepository mapRepository){
        return new MapInteractorImpl(mapRepository);
    }

    @Provides
    @Singleton
    MapRepository providesMapRepository(FirebaseAPI firebase, EventBus eventBus, AreasHelper areasHelper){
        return new MapRepositoryImpl(firebase, eventBus, areasHelper, cidade);
    }

    @Provides
    @Singleton
    AreasHelper providesAreasHelper(){
          String nomeDaClasse = "com.rsm.yuri.projecttaxilivre.map.models.Areas" + cidade +"Helper";
//        String nomeDaClasse = "com.rsm.yuri.projecttaxilivre.map.models.AreasTeresinaHelper";
//        String nomeDaClasse = "com.rsm.yuri.projecttaxilivre.map.models.AreasFortalezaHelper";
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
