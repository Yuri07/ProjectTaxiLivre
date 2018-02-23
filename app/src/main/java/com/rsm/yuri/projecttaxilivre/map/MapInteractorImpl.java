package com.rsm.yuri.projecttaxilivre.map;

import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;

/**
 * Created by yuri_ on 22/01/2018.
 */

public class MapInteractorImpl implements MapInteractor {

    MapRepository mapRepository;

    public MapInteractorImpl(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @Override
    public void subscribeForDriversUpdates() {
        mapRepository.subscribeForDriversEvents();
    }

    @Override
    public void unSubscribeForDriversUpdates() {
        mapRepository.unSubscribeForDriversEvents();
    }

    @Override
    public void destroyDriversListener() {
        mapRepository.destroyDriversListener();
    }

    /*@Override//nao tem essas funcoes disponiveis para o usuario na tela do mapfragment
    public void addNearDrivers() {
        mapRepository.addNearDrivers();
    }

    @Override
    public void removeNearDrivers() {
        mapRepository.removeNearDrivers();
    }*/

}
