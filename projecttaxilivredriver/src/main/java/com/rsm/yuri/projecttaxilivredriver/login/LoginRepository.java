package com.rsm.yuri.projecttaxilivredriver.login;

/**
 * Created by yuri_ on 12/01/2018.
 */

public interface LoginRepository {

    void signUp(final String email, final String password);
    void signIn(String email, String password);

}
