package com.rsm.yuri.projecttaxilivre.map;

import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapInteractor {

    void subscribeForDriversUpdates();
    void unSubscribeForDriversUpdates();
    void destroyDriversListener();

    /*void addNearDrivers();////nao tem essas funcoes disponiveis para o usuario na tela do mapfragment
    void removeNearDrivers();*/

}
