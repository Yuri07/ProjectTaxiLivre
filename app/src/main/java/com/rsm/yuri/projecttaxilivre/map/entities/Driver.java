package com.rsm.yuri.projecttaxilivre.map.entities;

import com.rsm.yuri.projecttaxilivre.R;

import java.util.Map;

/**
 * Created by yuri_ on 13/01/2018.
 */

public class Driver {

    private String email;

    private String nome;

    private String urlPhotoDriver;
    private double latitutde;
    private double longitude;

    private Car[] cars;

    private Rating[] ratings;

    private double averageRating;
    private int totalRatings;

    private int count5Stars;
    private int count4Stars;
    private int count3Stars;
    private int count2Stars;
    private int count1Stars;

    public final static String DRIVER_NAME_DEFAULT = "Driver";//getResources().getString(R.string.DRIVER_NAME_DEFAULT);//nao funciona pq nao tem contexto nesta classe Driver.

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

    public Car[] getCars() {
        return cars;
    }

    public void setCars(Car[] cars) {
        this.cars = cars;
    }

    public double getLatitutde() {
        return latitutde;
    }

    public void setLatitutde(double latitutde) {
        this.latitutde = latitutde;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getUrlPhotoDriver() {
        return urlPhotoDriver;
    }

    public void setUrlPhotoDriver(String urlPhotoDriver) {
        this.urlPhotoDriver = urlPhotoDriver;
    }

    public Rating[] getRatings() {
        return ratings;
    }

    public void setRatings(Rating[] ratings) {
        this.ratings = ratings;
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
