package com.rsm.yuri.projecttaxilivredriver.historicchatslist.entities;

import java.util.Map;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class Driver {

    private String email;

    private String nome;
    private String sobreNome;

    private String urlPhotoDriver;

    private double latitude;
    private double longitude;

    private long status;

    private double averageRating;
    private int totalRatings;

    private Map<String, Integer> historicChats;

    private int count5Stars;
    private int count4Stars;
    private int count3Stars;
    private int count2Stars;
    private int count1Stars;

    public final static String DRIVER_NAME_DEFAULT = "Driver";//getResources().getString(R.string.DRIVER_NAME_DEFAULT);//nao funciona pq nao tem contexto nesta classe Driver.
    public final static  int OFFLINE = 0;
    public final static  int ONLINE = 1;
    public final static  int WAITING_TRAVEL = 2;
    public final static  int IN_TRAVEL = 3;

    public Driver(){}

    public Driver(String email, int status, Map<String, Integer> historicChats){
        this.email = email;
        this.status = status;
        this.historicChats = historicChats;
        this.nome = "Usuario";
        this.urlPhotoDriver = "default";
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

    public String getSobreNome() {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome) {
        this.sobreNome = sobreNome;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public Map<String, Integer> getHistoricChats() {
        return historicChats;
    }

    public void setHistoricChats(Map<String, Integer> historicChats) {
        this.historicChats = historicChats;
    }

    public String getUrlPhotoDriver() {
        return urlPhotoDriver;
    }

    public void setUrlPhotoDriver(String urlPhotoDriver) {
        this.urlPhotoDriver = urlPhotoDriver;
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

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public int getCount5Stars() {
        return count5Stars;
    }

    public void setCount5Stars(int count5Stars) {
        this.count5Stars = count5Stars;
    }

    public int getCount4Stars() {
        return count4Stars;
    }

    public void setCount4Stars(int count4Stars) {
        this.count4Stars = count4Stars;
    }

    public int getCount3Stars() {
        return count3Stars;
    }

    public void setCount3Stars(int count3Stars) {
        this.count3Stars = count3Stars;
    }

    public int getCount2Stars() {
        return count2Stars;
    }

    public void setCount2Stars(int count2Stars) {
        this.count2Stars = count2Stars;
    }

    public int getCount1Stars() {
        return count1Stars;
    }

    public void setCount1Stars(int count1Stars) {
        this.count1Stars = count1Stars;
    }


}
