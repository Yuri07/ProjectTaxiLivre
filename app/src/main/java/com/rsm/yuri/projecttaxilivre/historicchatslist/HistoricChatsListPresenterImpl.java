package com.rsm.yuri.projecttaxilivre.historicchatslist;

import android.util.Log;

import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivre.historicchatslist.ui.HistoricChatsListView;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListPresenterImpl implements HistoricChatsListPresenter {

    EventBus eventBus;
    HistoricChatsListView historicChatsListView;
    HistoricChatsListInteractor historicChatsListInteractor;
    HistoricChatsListSessionInteractor sessionInteractor;

    public HistoricChatsListPresenterImpl(EventBus eventBus, HistoricChatsListView historicChatsListView, HistoricChatsListInteractor historicChatsListInteractor, HistoricChatsListSessionInteractor historicChatsListSessionInteractor) {
        this.eventBus = eventBus;
        this.historicChatsListView = historicChatsListView;
        this.historicChatsListInteractor = historicChatsListInteractor;
        this.sessionInteractor = historicChatsListSessionInteractor;
    }

    @Override
    public void onPause() {
        historicChatsListInteractor.unSubscribeForHistoricChatEvents();
        sessionInteractor.changeConnectionStatus(User.OFFLINE);

    }

    @Override
    public void onResume() {
        historicChatsListInteractor.subscribeForHistoricChatEvents();
        sessionInteractor.changeConnectionStatus(User.ONLINE);
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        historicChatsListInteractor.destroyHistoricChatListListener();
        historicChatsListView = null;
    }

    @Override
    public void removeHistoricChat(String email) {
        historicChatsListInteractor.removeHistoricChat(email);
    }

    @Override
    public void getUrlPhotoFromDriver(Driver driver) {
        historicChatsListInteractor.getUrlPhotoDriver(driver);
    }

    @Override
    @Subscribe
    public void onEventMainThread(HistoricChatsListEvent event) {
        Driver driver = event.getDriver();
        if (this.historicChatsListView!= null) {
            String error = event.getError();
            if (error != null) {
                historicChatsListView.onHistoricChatError(error);
            }else {
                switch (event.getEventType()) {
                    case HistoricChatsListEvent.onHistoricChatAdded:
                        Log.d("d", "event.type = historicchatAdded"+driver.getEmail());
                        onHistoricChatAdded(driver);
                        break;
                    case HistoricChatsListEvent.onHistoricChatChanged:
                        Log.d("d", "event.type = historicchatChanged"+driver.getEmail());
                        onHistoricChatChanged(driver);
                        break;
                    case HistoricChatsListEvent.onHistoricChatRemoved:
                        Log.d("d", "event.type = historicchatRemoved"+driver.getEmail());
                        onHistoricChatRemoved(driver);
                        break;
                    case HistoricChatsListEvent.onUrlPhotoDriverRetrived:
                        Log.d("d", "event.type = historicchatRemoved"+driver.getEmail());
                        onUrlPhotoDriverRetrived(driver);
                        break;
                }
            }
        }
    }

    private void onUrlPhotoDriverRetrived(Driver driver) {
        if (historicChatsListView != null) {
            historicChatsListView.onUrlPhotoDriverRetrived(driver);
        }
    }

    private void onHistoricChatRemoved(Driver driver) {
        if (historicChatsListView != null) {
            historicChatsListView.onHistoricChatRemoved(driver);
        }
    }

    private void onHistoricChatChanged(Driver driver) {
        if (historicChatsListView != null) {
            historicChatsListView.onHistoricChatChanged(driver);
        }
    }

    private void onHistoricChatAdded(Driver driver) {
        if (historicChatsListView != null) {
            historicChatsListView.onHistoricChatAdded(driver);
        }
    }
}
