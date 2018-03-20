package com.rsm.yuri.projecttaxilivredriver.historicchatslist;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListInteractor {
    void subscribeForHistoricChatEvents();
    void unSubscribeForHistoricChatEvents();
    void destroyHistoricChatListListener();
    void removeHistoricChat(String email);

    void getUrlPhotoUser(User user);
}
