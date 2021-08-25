package com.rsm.yuri.projecttaxilivre.main;

import android.util.Log;

import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.main.events.MainEvent;
import com.rsm.yuri.projecttaxilivre.main.ui.MainView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MainPresenterImpl implements MainPresenter {

    EventBus eventBus;
    MainView mainView;
    MainInteractor mainInteractor;
    SessionInteractor sessionInteractor;

    public MainPresenterImpl(EventBus eventBus, MainView mainView, MainInteractor mainInteractor,
                                                            SessionInteractor sessionInteractor) {
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
    public void onResume() {
        sessionInteractor.changeConnectionStatus(User.ONLINE);
        //mainInteractor.subscribeForDriversEvents();//essa função vai ser implementada por mapfragment
    }

    @Override
    public void onPause() {
        sessionInteractor.changeConnectionStatus(User.OFFLINE);
        //mainInteractor.unSubscribeForDriversEvents();//essa função vai ser implementada por mapfragment
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
        Log.d("d", "MainPresenter.onEventMainThread()");
        switch (event.getEventType()) {
            case MainEvent.onSuccessToRecoverSession:
                onSuccessToRecoverSession(event.getLoggedUser());
                break;
            case MainEvent.onFailedToRecoverSession:
                onFailedToRecoverSession();
                break;
            case MainEvent.onSucceessToSaveFirebaseTokenInServer:
                onSucceessToSaveFirebaseTokenInServer();
                break;
            case MainEvent.onFailedToSaveFirebaseTokenInServer:
                onFailedToSaveFirebaseTokenInServer(event.getErrorMessage());
                break;
        }
    }

    private void onSuccessToRecoverSession(User loggedUser) {
        if (mainView != null) {
            mainView.setLoggedUser(loggedUser);
            mainView.setUIVisibility(true);
        }
    }

    private void onFailedToRecoverSession() {
        if (mainView != null) {
            mainView.navigateToLoginScreen();
        }
    }

    private void onSucceessToSaveFirebaseTokenInServer(){
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

    /*@Override
    public void sendFirebaseNotificationTokenToServer(String firebaseNotificationToken) {
        mainInteractor.sendFirebaseNotificationTokenToServer(firebaseNotificationToken);
    }*/

    @Override
    public void verifyToken() {
        mainInteractor.verifyToken();
    }
}
