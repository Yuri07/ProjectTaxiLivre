package com.rsm.yuri.projecttaxilivre.login.di;

import com.rsm.yuri.projecttaxilivre.domain.FirebaseAPI;
import com.rsm.yuri.projecttaxilivre.lib.base.EventBus;
import com.rsm.yuri.projecttaxilivre.login.LoginInteractor;
import com.rsm.yuri.projecttaxilivre.login.LoginInteractorImpl;
import com.rsm.yuri.projecttaxilivre.login.LoginPresenter;
import com.rsm.yuri.projecttaxilivre.login.LoginPresenterImpl;
import com.rsm.yuri.projecttaxilivre.login.LoginRepository;
import com.rsm.yuri.projecttaxilivre.login.LoginRepositoryImpl;
import com.rsm.yuri.projecttaxilivre.login.SignupInteractor;
import com.rsm.yuri.projecttaxilivre.login.SignupInteractorImpl;
import com.rsm.yuri.projecttaxilivre.login.ui.LoginView;

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
