package com.rsm.yuri.projecttaxilivredriver.chat;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface ChatRepository {

    void sendMessage(String msg);
    void setReceiver(String receiver);

    void destroyChatListener();
    void subscribeForChatUpates();
    void unSubscribeForChatUpates();
    void subscribeForStatusReceiverUpdate();
    void unSubscribeForStatusReceiverUpdate();

    void changeUserConnectionStatus(long status);


}
