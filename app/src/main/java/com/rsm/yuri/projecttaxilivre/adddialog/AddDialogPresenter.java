package com.rsm.yuri.projecttaxilivre.adddialog;

import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogEvent.AddDialogEvent;

/**
 * Created by yuri_ on 10/11/2017.
 */

public interface AddDialogPresenter {

    void onShow();
    void onDestroy();

    void add(String key, String email);
    void onEventMainThread(AddDialogEvent event);

}
