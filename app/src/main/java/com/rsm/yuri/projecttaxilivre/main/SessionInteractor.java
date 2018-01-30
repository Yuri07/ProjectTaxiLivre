package com.rsm.yuri.projecttaxilivre.main;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface SessionInteractor {

    void logout();
    void checkForSession();
    void changeConnectionStatus(int status);

}
