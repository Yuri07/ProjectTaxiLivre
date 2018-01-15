package com.rsm.yuri.projecttaxilivre.map;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MapInteractor {


    void setRecipient(String recipient);

    void subscribeForMapUpdates();
    void unsubscribeForMapUpdates();
    void destroyMapListener();

}
