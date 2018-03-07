package com.rsm.yuri.projecttaxilivredriver.main.ui;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;

/**
 * Created by yuri_ on 05/03/2018.
 */

public interface MainView {

    void setUIVisibility(boolean enabled);

    //void checkForSession();
    void navigateToLoginScreen();
    void setLoggedUser(Driver loggedUser);

    void logout();

}
