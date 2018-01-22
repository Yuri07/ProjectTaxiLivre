package com.rsm.yuri.projecttaxilivre.main.ui;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MainView {

    void setUIVisibility(boolean enabled);

    void checkForSession();
    void navigateToLoginScreen();
    void setUserEmail(String email);

    void logout();


}
