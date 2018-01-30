package com.rsm.yuri.projecttaxilivre.historicchatslist.entities;

import com.rsm.yuri.projecttaxilivre.R;

import java.util.Map;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class User {

    private String email;
    private String nome;
    private int status;
    private String urlPhotoUser;
    private Map<String, Boolean> historicChats;

    public final static String USER_NAME_DEFAULT = "Usuário";
    public final static int OFFLINE = 0;
    public final static int ONLINE = 1;
    public final static int LOOKING_FOR_CAR = 2;
    public final static int WAITING_CAR = 3;
    public final static int IN_CAR = 4;

    public User(){ }

    public User(String email, int status, Map<String, Boolean> historicChats){
        this.email = email;
        this.status = status;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
