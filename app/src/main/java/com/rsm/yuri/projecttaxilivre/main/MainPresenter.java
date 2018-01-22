package com.rsm.yuri.projecttaxilivre.main;

import com.rsm.yuri.projecttaxilivre.main.events.MainEvent;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MainPresenter {

    void onCreate();
    void onDestroy();
    void onEventMainThread(MainEvent event);

    void logout();
    void checkForSession();

}
