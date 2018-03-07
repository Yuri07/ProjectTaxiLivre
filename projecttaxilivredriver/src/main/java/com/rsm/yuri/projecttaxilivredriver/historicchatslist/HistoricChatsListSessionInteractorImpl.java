package com.rsm.yuri.projecttaxilivredriver.historicchatslist;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class HistoricChatsListSessionInteractorImpl implements HistoricChatsListSessionInteractor {

    HistoricChatsListRepository historicChatsListRepository;

    public HistoricChatsListSessionInteractorImpl(HistoricChatsListRepository historicChatsListRepository) {
        this.historicChatsListRepository = historicChatsListRepository;
    }

    @Override
    public void changeConnectionStatus(int status) {
        historicChatsListRepository.changeUserConnectionStatus(status);
    }
}
