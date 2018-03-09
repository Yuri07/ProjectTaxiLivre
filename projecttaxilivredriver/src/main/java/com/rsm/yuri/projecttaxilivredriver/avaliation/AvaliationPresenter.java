package com.rsm.yuri.projecttaxilivredriver.avaliation;

import com.rsm.yuri.projecttaxilivredriver.avaliation.events.AvaliationEvent;

/**
 * Created by yuri_ on 07/03/2018.
 */

public interface AvaliationPresenter {

    void onCreate();
    void onDestroy();

    void getMyRatings(String email);

    void onEventMainThread(AvaliationEvent event);

}
