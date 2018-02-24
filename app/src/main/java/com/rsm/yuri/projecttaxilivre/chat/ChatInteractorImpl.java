package com.rsm.yuri.projecttaxilivre.chat;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class ChatInteractorImpl implements ChatInteractor {

    ChatRepository chatRepository;

    public ChatInteractorImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void subscribeForChatUpates() {
        chatRepository.subscribeForChatUpates();
    }

    @Override
    public void unSubscribeForChatUpates() {
        chatRepository.unSubscribeForChatUpates();
    }

    @Override
    public void destroyChatListener() {
        chatRepository.destroyChatListener();
    }

    @Override
    public void setRecipient(String recipient) {
        chatRepository.setReceiver(recipient);
    }

    @Override
    public void sendMessage(String msg) {
        chatRepository.sendMessage(msg);
    }
}
