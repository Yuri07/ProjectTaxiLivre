package com.rsm.yuri.projecttaxilivre.login;

import com.rsm.yuri.projecttaxilivre.login.events.LoginEvent;
import com.rsm.yuri.projecttaxilivre.login.ui.LoginView;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class LoginPresenterImpl implements LoginPresenter {

    //EventBus eventBus;
    LoginView loginView;
    LoginInteractor loginInteractor;
    SignupInteractor signupInteractor;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onEventMainThread(LoginEvent event) {

    }

    @Override
    public void login(String email, String password) {

    }

    @Override
    public void registerNewUser(String email, String password) {

    }
}
