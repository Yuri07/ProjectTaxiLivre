package com.rsm.yuri.projecttaxilivredriver.chat;

import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.chat.entities.ChatMessage;
import com.rsm.yuri.projecttaxilivredriver.chat.events.ChatEvent;
import com.rsm.yuri.projecttaxilivredriver.chat.ui.ChatView;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class ChatPresenterImpl implements ChatPresenter {

    EventBus eventBus;
    ChatView chatView;
    ChatInteractor chatInteractor;
    ChatSessionInteractor chatSessionInteractor;

    public ChatPresenterImpl(EventBus eventBus, ChatView chatView, ChatInteractor chatInteractor, ChatSessionInteractor chatSessionInteractor) {
        this.eventBus = eventBus;
        this.chatView = chatView;
        this.chatInteractor = chatInteractor;
        this.chatSessionInteractor = chatSessionInteractor;
    }

    @Override
    public void onPause() {
        chatInteractor.unSubscribeForChatUpates();
        chatInteractor.unSubscribeForStatusReceiverUpdate();
        chatSessionInteractor.changeConnectionStatus(User.OFFLINE);
    }

    @Override
    public void onResume() {
        chatInteractor.subscribeForChatUpates();
        chatInteractor.subscribeForStatusReceiverUpdate();
        chatSessionInteractor.changeConnectionStatus(User.ONLINE);
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        chatInteractor.destroyChatListener();
        chatView = null;
    }

    @Override
    public void setChatRecipient(String recipient) {
        this.chatInteractor.setRecipient(recipient);
    }

    @Override
    public void sendMessage(String msg) {
        chatInteractor.sendMessage(msg);
    }

    @Override
    @Subscribe
    public void onEventMainThread(ChatEvent event) {
        //Log.d("d", "postOnChildAdd = " + event.getMsg().getMsg());
        if (chatView != null) {
            if (event.getEventType() == ChatEvent.READ_EVENT){
                ChatMessage msg = event.getMsg();
                chatView.onMessageReceived(msg);
            }else if(event.getEventType() == ChatEvent.READ_STATUS_RECEIVER_EVENT){
                chatView.onStatusChanged(event.getStatusReceiver());
            }
        }
    }
}
