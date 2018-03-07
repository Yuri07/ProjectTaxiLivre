package com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface OnItemClickListener {

    void onItemClick(User user);
    void onItemLongClick(User user);

}
