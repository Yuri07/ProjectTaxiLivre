package com.rsm.yuri.projecttaxilivre.historicchatslist;

import com.rsm.yuri.projecttaxilivre.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

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

    void getUrlPhotoFromDriver(Driver driver);
}
