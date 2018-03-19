package com.rsm.yuri.projecttaxilivredriver.main;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MainInteractorImpl implements MainInteractor {

    MainRepository mainRepository;

    public MainInteractorImpl(MainRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    @Override
    public void setRecipient(String recipient) {

    }

    @Override
    public void getMyCar() {
        mainRepository.getMyCar();
    }

}
