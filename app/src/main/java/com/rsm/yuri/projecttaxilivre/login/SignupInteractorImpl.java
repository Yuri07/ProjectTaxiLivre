package com.rsm.yuri.projecttaxilivre.login;

/**
 * Created by yuri_ on 12/01/2018.
 */

public class SignupInteractorImpl implements SignupInteractor {

    private LoginRepository loginRepository;

    public SignupInteractorImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public void execute(String email, String password) {
        loginRepository.signUp(email, password);
    }
}
