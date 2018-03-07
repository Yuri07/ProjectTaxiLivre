package com.rsm.yuri.projecttaxilivredriver.login.di;

import com.rsm.yuri.projecttaxilivredriver.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivredriver.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivredriver.login.LoginInteractor;
import com.rsm.yuri.projecttaxilivredriver.login.LoginInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.login.LoginPresenter;
import com.rsm.yuri.projecttaxilivredriver.login.LoginPresenterImpl;
import com.rsm.yuri.projecttaxilivredriver.login.LoginRepository;
import com.rsm.yuri.projecttaxilivredriver.login.LoginRepositoryImpl;
import com.rsm.yuri.projecttaxilivredriver.login.SignupInteractor;
import com.rsm.yuri.projecttaxilivredriver.login.SignupInteractorImpl;
import com.rsm.yuri.projecttaxilivredriver.login.ui.LoginView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by yuri_ on 12/01/2018.
 */
@Module
public class LoginModule {

    LoginView view;

    public LoginModule(LoginView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    LoginView providesLoginView(){
        return this.view;
    }

    @Provides
    @Singleton
    LoginPresenter providesLoginPresenter(EventBus eventBus, LoginView loginView, LoginInteractor loginInteractor, SignupInteractor signupInteractor) {
        return new LoginPresenterImpl(eventBus, loginView, loginInteractor, signupInteractor);
    }

    @Provides @Singleton
    LoginInteractor providesLoginInteractor(LoginRepository repository) {
        return new LoginInteractorImpl(repository);
    }

    @Provides @Singleton
    SignupInteractor providesSignupInteractor(LoginRepository repository) {
        return new SignupInteractorImpl(repository);
    }

    @Provides @Singleton
    LoginRepository providesLoginRepository(FirebaseAPI firebase, EventBus eventBus) {
        return new LoginRepositoryImpl(firebase, eventBus);
    }

}
