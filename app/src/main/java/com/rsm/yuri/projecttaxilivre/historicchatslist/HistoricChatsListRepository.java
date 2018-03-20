package com.rsm.yuri.projecttaxilivre.historicchatslist;

import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListRepository {

    void removeHistoricChat(String email);
    void destroyHistoricChatListListener();
    void subscribeForHistoricChatListUpdates();
    void unSubscribeForHistoricChatListUpdates();

    void changeUserConnectionStatus(int status);

    void getUrlPhotoDriver(Driver driver);
    //void changeUserConnectionStatus(int status);//essa funcao esta implentada na MainActivity

}
