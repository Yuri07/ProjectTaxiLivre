package com.rsm.yuri.projecttaxilivredriver.main;

import com.rsm.yuri.projecttaxilivredriver.main.events.MainEvent;

/**
 * Created by yuri_ on 02/03/2018.
 */

public interface MainPresenter {

    void onCreate();
    void onResume();
    void onPause();
    void onDestroy();
    void onEventMainThread(MainEvent event);

    void logout();
    void checkForSession();

}
