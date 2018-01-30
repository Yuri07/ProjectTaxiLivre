package com.rsm.yuri.projecttaxilivre.map;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapInteractor {

    void subscribeForDriversUpdates();
    void unsubscribeForDriversUpdates();
    void destroyDriversListener();



}
