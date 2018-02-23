package com.rsm.yuri.projecttaxilivre.historicchatslist.ui;

import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListView {

    void onHistoricChatAdded(Driver driver);
    void onHistoricChatChanged(Driver driver);
    void onHistoricChatRemoved(Driver driver);
    void onHistoricChatError(String error);

}
