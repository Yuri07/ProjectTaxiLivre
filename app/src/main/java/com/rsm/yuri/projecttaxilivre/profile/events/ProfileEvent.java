package com.rsm.yuri.projecttaxilivre.profile.events;

import com.rsm.yuri.projecttaxilivre.historicchatslist.entities.User;

/**
 * Created by yuri_ on 27/02/2018.
 */

public class ProfileEvent {

    private String ulrPhotoUser;
    private int type;
    private String error;
    private User currentUser;

    public final static int UPLOAD_INIT = 0;
    public final static int UPLOAD_COMPLETE = 1;
    public final static int UPLOAD_ERROR = 2;

    public final static int onSuccessToGetDateUser = 3;
    public final static int onFailedToGetDateUser = 4;

    public String getUlrPhotoUser() {
        return ulrPhotoUser;
    }

    public void setUlrPhotoUser(String ulrPhotoUser) {
        this.ulrPhotoUser = ulrPhotoUser;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
