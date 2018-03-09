package com.rsm.yuri.projecttaxilivredriver.avaliation;

/**
 * Created by yuri_ on 07/03/2018.
 */

public class AvaliationInteractorImpl implements AvaliationInteractor {

    private AvaliationRepository avaliationRepository;

    public AvaliationInteractorImpl(AvaliationRepository avaliationRepository) {
        this.avaliationRepository = avaliationRepository;
    }

    @Override
    public void execute(String email) {
        avaliationRepository.retrieveRatings(email);
    }
}
