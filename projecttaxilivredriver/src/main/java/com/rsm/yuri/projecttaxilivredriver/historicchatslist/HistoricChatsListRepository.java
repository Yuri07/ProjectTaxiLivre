package com.rsm.yuri.projecttaxilivredriver.historicchatslist;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListRepository {

    void removeHistoricChat(String email);
    void destroyHistoricChatListListener();
    void subscribeForHistoricChatListUpdates();
    void unSubscribeForHistoricChatListUpdates();

    void changeUserConnectionStatus(int status);
    void getUrlPhotoUser(User user);

}
