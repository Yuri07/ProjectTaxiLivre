package com.rsm.yuri.projecttaxilivre.main;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MainRepository {

    void logout();
    void checkForSession();
    void changeUserConnectionStatus(int status);

    void verifyToken();

    //void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken);
                                                    //FirebaseInstanceId obsoleto, agora é
                                                    //usado pelo verifyToken()

}
