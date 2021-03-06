package com.rsm.yuri.projecttaxilivredriver.main;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class SessionInteractorImpl implements SessionInteractor {

    MainRepository mainRepository;

    public SessionInteractorImpl(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public void logout() {
        mainRepository.logout();
    }

    @Override
    public void checkForSession() {
        mainRepository.checkForSession();
    }

    @Override
    public void changeConnectionStatus(int status) {
        mainRepository.changeUserConnectionStatus(status);
    }
}
