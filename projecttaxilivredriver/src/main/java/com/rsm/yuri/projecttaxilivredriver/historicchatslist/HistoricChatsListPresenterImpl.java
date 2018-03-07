package com.rsm.yuri.projecttaxilivredriver.historicchatslist;

import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.events.HistoricChatsListEvent;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.ui.HistoricChatsListView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;

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
    @Subscribe
    public void onEventMainThread(HistoricChatsListEvent event) {
        User user = event.getUser();
        if (this.historicChatsListView!= null) {
            String error = event.getError();
            if (error != null) {
                historicChatsListView.onHistoricChatError(error);
            }else {
                switch (event.getEventType()) {
                    case HistoricChatsListEvent.onHistoricChatAdded:
                        Log.d("d", "event.type = historicchatAdded"+user.getEmail());
                        onHistoricChatAdded(user);
                        break;
                    case HistoricChatsListEvent.onHistoricChatChanged:
                        Log.d("d", "event.type = historicchatChanged"+user.getEmail());
                        onHistoricChatChanged(user);
                        break;
                    case HistoricChatsListEvent.onHistoricChatRemoved:
                        Log.d("d", "event.type = historicchatRemoved"+user.getEmail());
                        onHistoricChatRemoved(user);
                        break;
                }
            }
        }
    }

    private void onHistoricChatRemoved(User user) {
        if (historicChatsListView != null) {
            historicChatsListView.onHistoricChatRemoved(user);
        }
    }

    private void onHistoricChatChanged(User user) {
        if (historicChatsListView != null) {
            historicChatsListView.onHistoricChatChanged(user);
        }
    }

    private void onHistoricChatAdded(User user) {
        if (historicChatsListView != null) {
            historicChatsListView.onHistoricChatAdded(user);
        }
    }
}
