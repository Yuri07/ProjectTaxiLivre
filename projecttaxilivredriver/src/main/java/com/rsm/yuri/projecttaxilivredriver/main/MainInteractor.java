package com.rsm.yuri.projecttaxilivredriver.main;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MainInteractor {

    void setRecipient(String recipient);
    void getMyCar();
    void changeWaitingTravelStatus(int status);
    void uploadCompletedTravelStatus(int status);
    void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken);

}
