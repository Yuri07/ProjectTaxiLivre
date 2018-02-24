package com.rsm.yuri.projecttaxilivre.chat;

import com.rsm.yuri.projecttaxilivre.chat.events.ChatEvent;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface ChatPresenter {

    void onPause();
    void onResume();
    void onCreate();
    void onDestroy();

    void setChatRecipient(String recipient);

    void sendMessage(String msg);
    void onEventMainThread(ChatEvent event);

}
