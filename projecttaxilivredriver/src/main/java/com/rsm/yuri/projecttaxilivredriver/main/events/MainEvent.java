package com.rsm.yuri.projecttaxilivredriver.main.events;

import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.Driver;
import com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities.User;

/**
 * Created by yuri_ on 15/01/2018.
 */

public class MainEvent {

    private int eventType;

    private Driver loggedUser;

    private String errorMessage;

   // private String loggedUserEmail;

    public final static int READ_DRIVER_EVENT = 0;

    public final static int onSuccessToRecoverSession = 10;
    public final static int onFailedToRecoverSession = 11;

    public Driver getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(Driver loggedUser) {
        this.loggedUser = loggedUser;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /*public String getLoggedUserEmail() {
        return loggedUserEmail;
    }

    public void setLoggedUserEmail(String loggedUserEmail) {
        this.loggedUserEmail = loggedUserEmail;
    }*/
}
