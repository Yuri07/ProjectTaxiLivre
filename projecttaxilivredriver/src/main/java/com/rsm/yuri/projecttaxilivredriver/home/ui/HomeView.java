package com.rsm.yuri.projecttaxilivredriver.home.ui;

/**
 * Created by yuri_ on 09/03/2018.
 */

public interface HomeView {
    void onLocationReadingError(String error);

    void onTravelCreated(String newTravelID);

}
