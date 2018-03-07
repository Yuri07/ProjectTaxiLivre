package com.rsm.yuri.projecttaxilivredriver.historicchatslist;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListInteractor {
    void subscribeForHistoricChatEvents();
    void unSubscribeForHistoricChatEvents();
    void destroyHistoricChatListListener();
    void removeHistoricChat(String email);
}
