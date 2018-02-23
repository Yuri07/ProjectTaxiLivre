package com.rsm.yuri.projecttaxilivre.map;

import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapRepository {

    void subscribeForDriversEvents();
    void unSubscribeForDriversEvents();
    void destroyDriversListener();

    //void addNearDrivers();//nao tem essas funções para o usuario na tela do mapfragment.
    //void removeNearDrivers();

}
