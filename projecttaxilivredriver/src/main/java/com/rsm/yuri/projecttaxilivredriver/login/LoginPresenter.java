package com.rsm.yuri.projecttaxilivredriver.login;

import com.rsm.yuri.projecttaxilivredriver.login.events.LoginEvent;

/**
 * Created by yuri_ on 12/01/2018.
 */

public interface LoginPresenter {

    void onCreate();
    void onDestroy();
    void onEventMainThread(LoginEvent event);
    void login(String email, String password);
    void registerNewUser(String email, String password);

}
