package com.rsm.yuri.projecttaxilivre.main.ui;

import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;

/**
 * Created by yuri_ on 15/01/2018.
 */

public interface MainView {

    void setUIVisibility(boolean enabled);

    //void checkForSession();
    void navigateToLoginScreen();
    void setLoggedUser(User loggedUser);

    void logout();


}
