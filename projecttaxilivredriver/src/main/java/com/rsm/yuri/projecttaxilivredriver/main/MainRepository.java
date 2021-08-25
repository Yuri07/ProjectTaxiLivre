package com.rsm.yuri.projecttaxilivredriver.main;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MainRepository {

    void logout();
    void checkForSession();
    void changeUserConnectionStatus(int status);
    //void uploadCompletedTravelStatus(int status);
    void getMyCar();
    //void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken);

    void verifyToken();
}
