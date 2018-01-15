package com.rsm.yuri.projecttaxilivre.map;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class SessionInteractorImpl implements SessionInteractor {

    MapRepository mapRepository;

    public SessionInteractorImpl(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @Override
    public void logout() {
        mapRepository.logout();
    }

    @Override
    public void checkForSession() {
        mapRepository.checkForSession();
    }
}
