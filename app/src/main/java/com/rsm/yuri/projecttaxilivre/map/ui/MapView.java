package com.rsm.yuri.projecttaxilivre.map.ui;

import com.google.android.gms.maps.model.LatLng;
import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;
import com.rsm.yuri.projecttaxilivre.map.entities.NearDriver;

/**
 * Created by yuri_ on 22/01/2018.
 */

public interface MapView {

    void onDriverMoved(NearDriver neardriver);
    void onDriverAdded(NearDriver neardriver);
    void onDriverRemoved(NearDriver neardriver);
    void onDriverError(String error);

    void onTravelAckReceived(String travelAck);

    void onMyDriverMoved(LatLng locationOfMyDriver);

    void onSuccessToGetDataUser(User currentUser);
}
