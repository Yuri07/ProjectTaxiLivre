package com.rsm.yuri.projecttaxilivredriver.historicchatslist;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.events.HistoricChatsListEvent;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface HistoricChatsListPresenter {

    void onPause();
    void onResume();
    void onCreate();
    void onDestroy();

    void removeHistoricChat(String email);
    void onEventMainThread(HistoricChatsListEvent event);

}
