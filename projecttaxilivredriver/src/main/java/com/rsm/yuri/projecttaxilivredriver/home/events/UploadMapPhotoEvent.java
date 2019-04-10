package com.rsm.yuri.projecttaxilivredriver.home.events;

public class UploadMapPhotoEvent {

    private String ulrPhoto;
    private int type;
    private String error;
    public final static int UPLOAD_INIT = 0;
    public final static int UPLOAD_PHOTO_MAP_COMPLETE = 1;
    public final static int UPLOAD_ERROR = 2;
    public final static int SAVE_TRAVEL_SUCESS = 3;
    public final static int SAVE_TRAVEL_ERROR = 4;

}
