package com.rsm.yuri.projecttaxilivre.historicchatslist;

import com.rsm.yuri.projecttaxilivre.map.entities.Driver;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class HistoricChatsListInteractorImpl implements HistoricChatsListInteractor {

    HistoricChatsListRepository historicChatsListRepository;

    public HistoricChatsListInteractorImpl(HistoricChatsListRepository historicChatsListRepository) {
        this.historicChatsListRepository = historicChatsListRepository;
    }

    @Override
    public void subscribeForHistoricChatEvents() {
        historicChatsListRepository.subscribeForHistoricChatListUpdates();
    }

    @Override
    public void unSubscribeForHistoricChatEvents() {
        historicChatsListRepository.unSubscribeForHistoricChatListUpdates();
    }

    @Override
    public void destroyHistoricChatListListener() {
        historicChatsListRepository.destroyHistoricChatListListener();
    }

    @Override
    public void removeHistoricChat(String email) {
        historicChatsListRepository.removeHistoricChat(email);
    }

    @Override
    public void getUrlPhotoDriver(Driver driver) {
        historicChatsListRepository.getUrlPhotoDriver(driver);
    }
}
