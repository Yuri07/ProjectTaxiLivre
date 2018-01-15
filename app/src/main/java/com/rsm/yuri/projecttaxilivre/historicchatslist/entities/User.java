package com.rsm.yuri.projecttaxilivre.historicchatslist.entities;

import java.util.Map;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class User {

    private String email;
    private boolean online;
    private String urlPhotoDriver;
    private Map<String, Boolean> historicChats;
    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;

    public User(){ }

    public User(String email, boolean online, Map<String, Boolean> historicChats){
        this.email = email;
        this.online = online;
        this.historicChats = historicChats;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlPhotoDriver() {
        return urlPhotoDriver;
    }

    public void setUrlPhotoDriver(String urlPhotoDriver) {
        this.urlPhotoDriver = urlPhotoDriver;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Map<String, Boolean> getContacts() {
        return historicChats;
    }

    public void setContacts(Map<String, Boolean> historicChats) {
        this.historicChats= historicChats;
    }

}
