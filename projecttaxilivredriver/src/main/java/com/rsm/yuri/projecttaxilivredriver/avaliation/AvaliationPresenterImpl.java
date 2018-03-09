package com.rsm.yuri.projecttaxilivredriver.avaliation;

import com.rsm.yuri.projecttaxilivredriver.avaliation.entities.Rating;
import com.rsm.yuri.projecttaxilivredriver.avaliation.events.AvaliationEvent;
import com.rsm.yuri.projecttaxilivredriver.avaliation.ui.AvaliationView;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 07/03/2018.
 */

public class AvaliationPresenterImpl implements AvaliationPresenter {

    EventBus eventBus;
    AvaliationView avaliationView;
    AvaliationInteractor avaliationInteractor;

    public AvaliationPresenterImpl(EventBus eventBus, AvaliationView avaliationView, AvaliationInteractor avaliationInteractor) {
        this.eventBus = eventBus;
        this.avaliationView = avaliationView;
        this.avaliationInteractor = avaliationInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
    }

    @Override
    public void getMyRatings(String email){
        avaliationInteractor.execute(email);
    }

    @Override
    @Subscribe
    public void onEventMainThread(AvaliationEvent event) {
        Rating rating = event.getRating();
        if (this.avaliationView!= null) {
            String error = event.getError();
            if (error != null) {
                avaliationView.onRatingError(error);
            }else{
                avaliationView.onRatingAdded(rating);
            }
        }
    }
}
