package com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities;

import java.util.Map;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class User {

    private String email;
    private String nome;
    private long status;
    private double latitude;
    private double longitude;
    private String urlPhotoUser;
    private Map<String, Integer> historicChats;
    private Map<String, Boolean> ratings;

    public final static String USER_NAME_DEFAULT = "Usuário";
    public final static int OFFLINE = 0;
    public final static int ONLINE = 1;
    public final static int LOOKING_FOR_CAR = 2;
    public final static int WAITING_CAR = 3;
    public final static int IN_CAR = 4;

    public User(){ }

    public User(String email, int status, Map<String, Integer> historicChats){
        this.email = email;
        this.status = status;
        this.historicChats = historicChats;
        this.nome = "Usuario";
        this.urlPhotoUser = "default";
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Map<String, Integer> getHistoricChats() {
        return historicChats;
    }

    public void setHistoricChats(Map<String, Integer> historicChats) {
        this.historicChats = historicChats;
    }

    public String getUrlPhotoUser() {
        return urlPhotoUser;
    }

    public void setUrlPhotoUser(String urlPhotoUser) {
        this.urlPhotoUser = urlPhotoUser;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Map<String, Boolean> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Boolean> ratings) {
        this.ratings = ratings;
    }
}
