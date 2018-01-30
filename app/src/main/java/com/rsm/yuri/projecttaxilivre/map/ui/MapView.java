package com.rsm.yuri.projecttaxilivre.map.ui;

import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapView {

    void onDriverMoved(Driver driver);
    void onDriverAdded(Driver driver);
    void onDriverRemoved(Driver driver);
    void onDriverError(String error);

}
