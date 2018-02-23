package com.rsm.yuri.projecttaxilivre.historicchatslist;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListRepository {

    void removeHistoricChat(String email);
    void destroyHistoricChatListListener();
    void subscribeForHistoricChatListUpdates();
    void unSubscribeForHistoricChatListUpdates();

    void changeUserConnectionStatus(int status);
    //void changeUserConnectionStatus(int status);//essa funcao esta implentada na MainActivity

}
