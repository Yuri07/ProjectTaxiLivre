package com.rsm.yuri.projecttaxilivre.map.ui;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MapView {

    void setUIVisibility(boolean enabled);

    void checkForSession();
    void navigateToLoginScreen();
    void setUserEmail(String email);

    void logout();


}
