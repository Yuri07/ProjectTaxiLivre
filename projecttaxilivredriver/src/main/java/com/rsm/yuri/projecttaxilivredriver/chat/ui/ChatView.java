package com.rsm.yuri.projecttaxilivredriver.chat.ui;

import com.rsm.yuri.projecttaxilivredriver.chat.entities.ChatMessage;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface ChatView  {
    void onMessageReceived(ChatMessage msg);
    void onStatusChanged(long online);
}
