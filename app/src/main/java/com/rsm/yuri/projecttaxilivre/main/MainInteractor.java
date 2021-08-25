package com.rsm.yuri.projecttaxilivre.main;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MainInteractor {

    void setRecipient(String recipient);

    //void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken);
                                                    ////FirebaseInstanceId obsoleto
    void verifyToken();
}
