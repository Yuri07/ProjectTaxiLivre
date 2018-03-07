package com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListView {

    void onHistoricChatAdded(User user);
    void onHistoricChatChanged(User user);
    void onHistoricChatRemoved(User user);
    void onHistoricChatError(String error);

}
