package com.rsm.yuri.projecttaxilivredriver.main;

import com.rsm.yuri.projecttaxilivredriver.main.events.MainEvent;

/**
 * Created by yuri_ on 02/03/2018.
 */

public interface MainPresenter {

    void onCreate();
    void changeToOnlineStatus();
    void changeToOfflineStatus();
    void onDestroy();
    void onEventMainThread(MainEvent event);

    void startWaitingTravel();
    void stopWaitingTravel();

    void logout();
    void checkForSession();

    void getMyCar();

    void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken);

}
