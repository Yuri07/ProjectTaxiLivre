package com.rsm.yuri.projecttaxilivre.chat;

/**
 * Created by yuri_ on 13/01/2018.
 */

public interface ChatInteractor {

    void sendMessage(String msg);
    void setRecipient(String recipient);

    void destroyChatListener();
    void subscribeForChatUpates();
    void unSubscribeForChatUpates();

}
