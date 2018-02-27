package com.rsm.yuri.projecttaxilivre.adddialog;

/**
 * Created by yuri_ on 10/11/2017.
 */

public class AddDialogInteractorImpl implements AddDialogInteractor {
    AddDialogRepository addDialogRepository;

    public AddDialogInteractorImpl(AddDialogRepository addDialogRepository) {
        this.addDialogRepository = addDialogRepository;
    }

    @Override
    public void add(String key, String value) {
        addDialogRepository.add(key, value);
    }
}
