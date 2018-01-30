package com.rsm.yuri.projecttaxilivre.historicchatslist.entities;

import com.rsm.yuri.projecttaxilivre.R;

import java.util.Map;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class User {

    private String email;
    private String nome;
    private boolean online;
    private String urlPhotoUser;
    private Map<String, Boolean> historicChats;

    public final static String USER_NAME_DEFAULT = "Usuário";
    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;

    public User(){ }

    public User(String email, boolean online, Map<String, Boolean> historicChats){
        this.email = email;
        this.online = online;
        this.historicChats = historicChats;
        this.nome = "Usuario";
        this.urlPhotoUser = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String, Boolean> getHistoricChats() {
        return historicChats;
    }

    public void setHistoricChats(Map<String, Boolean> historicChats) {
        this.historicChats = historicChats;
    }

    public String getUrlPhotoUser() {
        return urlPhotoUser;
    }

    public void setUrlPhotoUser(String urlPhotoUser) {
        this.urlPhotoUser = urlPhotoUser;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

}
