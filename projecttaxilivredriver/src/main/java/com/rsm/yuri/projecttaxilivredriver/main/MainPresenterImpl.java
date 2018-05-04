package com.rsm.yuri.projecttaxilivredriver.main;

import android.util.Log;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Car;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.main.events.MainEvent;
import com.rsm.yuri.projecttaxilivredriver.main.ui.MainView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MainPresenterImpl implements MainPresenter {

    EventBus eventBus;
    MainView mainView;
    MainInteractor mainInteractor;
    SessionInteractor sessionInteractor;

    public MainPresenterImpl(EventBus eventBus, MainView mainView, MainInteractor mainInteractor, SessionInteractor sessionInteractor) {
        this.eventBus = eventBus;
        this.mainView = mainView;
        this.mainInteractor = mainInteractor;
        this.sessionInteractor = sessionInteractor;
    }

    @Override
    public void onCreate() {
        eventBus.register(this);
    }

    @Override
    public void changeToOnlineStatus() {
        sessionInteractor.changeConnectionStatus(User.ONLINE);
    }

    @Override
    public void changeToOfflineStatus() {
        sessionInteractor.changeConnectionStatus(User.OFFLINE);
    }

    @Override
    public void startWaitingTravel() {
        mainInteractor.changeWaitingTravelStatus(Driver.WAITING_TRAVEL);
    }

    @Override
    public void stopWaitingTravel() {
        mainInteractor.changeWaitingTravelStatus(Driver.ONLINE);
    }

    @Override
    public void onDestroy() {
        mainView = null;
        eventBus.unregister(this);
        //mainInteractor.destroyDriversListListener();//essa função vai ser implementada por mapfragment

    }

    @Override
    @Subscribe
    public void onEventMainThread(MainEvent event) {
        switch (event.getEventType()) {
            case MainEvent.onSuccessToRecoverSession:
                onSuccessToRecoverSession(event.getLoggedUser());
                break;
            case MainEvent.onFailedToRecoverSession:
                //Log.d("d", "onEventMainThread:onFailedToRecoverSession " );
                onFailedToRecoverSession();
                break;
            case MainEvent.onSuccessToRecoverMyCar:
                onSuccessToRecoverMyCar(event.getMyCar());
                break;
            case MainEvent.onFailedToRecoverMyCar:
                onFailedToRecoverMyCar(event.getErrorMessage());
                break;
            case MainEvent.onSucceessToSaveFirebaseTokenInServer:
                onSuccessToSaveFirebaseTokenInServer();
                break;
            case MainEvent.onFailedToSaveFirebaseTokenInServer:
                onFailedToSaveFirebaseTokenInServer(event.getErrorMessage());
                break;
        }
    }



    private void onSuccessToRecoverSession(Driver loggedUser) {
        if (mainView != null) {
            mainView.setLoggedUser(loggedUser);
            mainView.setUIVisibility(true);
        }
    }

    private void onFailedToRecoverSession() {
        if (mainView != null) {
            //Log.d("d", "onFailedToRecoverSession() " );
            mainView.navigateToLoginScreen();
        }
    }

    private void onSuccessToRecoverMyCar(Car myCar) {
        if (mainView != null) {
            mainView.setMyCar(myCar);
        }
    }

    private void onFailedToRecoverMyCar(String errorMessage) {
        if (mainView != null) {
            mainView.onFailedToRecoverMyCar(errorMessage);
        }
    }

    private void onSuccessToSaveFirebaseTokenInServer(){
        mainView.onSucceessToSaveFirebaseTokenInServer();
    }

    private void onFailedToSaveFirebaseTokenInServer(String errorMessage){
        mainView.onFailedToSaveFirebaseTokenInServer(errorMessage);
    }

    @Override
    public void logout() {
        sessionInteractor.changeConnectionStatus(User.OFFLINE);
        sessionInteractor.logout();
    }

    @Override
    public void checkForSession() {
        sessionInteractor.checkForSession();
    }

    @Override
    public void getMyCar() {
        mainInteractor.getMyCar();
    }

    @Override
    public void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken) {
        mainInteractor.sendFirebaseNotificationTokenToServer(firebaseNotificationToken);
    }
}
