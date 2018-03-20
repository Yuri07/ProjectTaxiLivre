package com.rsm.yuri.projecttaxilivre.historicchatslist;

import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListInteractor {
    void subscribeForHistoricChatEvents();
    void unSubscribeForHistoricChatEvents();
    void destroyHistoricChatListListener();
    void removeHistoricChat(String email);

    void getUrlPhotoDriver(Driver driver);
}
