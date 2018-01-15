package com.rsm.yuri.projecttaxilivre.map;

import com.rsm.yuri.projecttaxilivre.login.events.LoginEvent;
import com.rsm.yuri.projecttaxilivre.map.events.MapEvent;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MapPresenter {

    void onCreate();
    void onDestroy();
    void onEventMainThread(MapEvent event);

    void logout();
    void checkForSession();

}
