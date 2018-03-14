package com.rsm.yuri.projecttaxilivredriver.avaliation.ui;

import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;

/**
 * Created by yuri_ on 07/03/2018.
 */

public interface AvaliationView {
    void onRatingAdded(Rating rating);
    void onRatingError(String error);

}
