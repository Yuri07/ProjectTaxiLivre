package com.rsm.yuri.projecttaxilivre.chat;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class ChatSessionInteractorImpl implements ChatSessionInteractor {
    ChatRepository chatRepository;

    public ChatSessionInteractorImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void changeConnectionStatus(long status) {
        chatRepository.changeUserConnectionStatus(status);
    }
}
