package com.rsm.yuri.projecttaxilivre.adddialog;

import android.util.Log;

import com.rsm.yuri.projecttaxilivre.adddialog.AddDialogEvent.AddDialogEvent;
import com.rsm.yuri.projecttaxilivre.adddialog.ui.AddDialogView;
import com.rsm.yuri.projecttaxilivre.lib.GreenRobotEventBus;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;



/**
 * Created by yuri_ on 10/11/2017.
 */

public class AddDialogPresenterImpl implements AddDialogPresenter {

    EventBus eventBus;
    AddDialogView addDialogView;
    AddDialogInteractor addDialogInteractor;

    public AddDialogPresenterImpl(EventBus eventBus, AddDialogView addDialogView, AddDialogInteractor addDialogInteractor) {
        this.eventBus = eventBus;
        this.addDialogView = addDialogView;
        this.addDialogInteractor = addDialogInteractor;
    }

    @Override
    public void onShow() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        addDialogView = null;
        eventBus.unregister(this);
    }

    @Override
    public void add(String key, String email) {
        addDialogView.hideInput();
        addDialogView.showProgress();
        this.addDialogInteractor.add(key, email);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AddDialogEvent event) {
        if (addDialogView != null) {
            addDialogView.hideProgress();
            addDialogView.showInput();

            if (event.getErrorMsg()==null) {
                addDialogView.contactAdded();
            } else {
                addDialogView.contactNotAdded();
                Log.d("d", event.getErrorMsg());
            }
        }
    }
}
